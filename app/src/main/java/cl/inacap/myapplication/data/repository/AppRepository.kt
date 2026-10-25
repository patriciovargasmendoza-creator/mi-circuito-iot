package cl.inacap.myapplication.data.repository

import android.content.Context
import android.util.Log
import cl.inacap.myapplication.data.model.ACCION_ACTIVAR
import cl.inacap.myapplication.data.model.ACCION_DESACTIVAR
import cl.inacap.myapplication.data.model.ACCION_OBSTACULO
import cl.inacap.myapplication.data.model.CircuitAction
import cl.inacap.myapplication.data.model.CircuitStats
import cl.inacap.myapplication.data.model.User
import cl.inacap.myapplication.util.PasswordHasher
import cl.inacap.myapplication.util.RutUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Estructura en Firebase Realtime Database:
 *  users/{rut sin puntos ni guión}   -> perfil, passwordHash y salt
 *  circuit/active                    -> Boolean, estado del circuito
 *  sensors/distanceCm                -> Long, lectura del sensor (la escribe el dispositivo IoT)
 *  actions/{id}                      -> registro de cada acción
 */
class AppRepository private constructor(context: Context) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isCircuitActive = MutableStateFlow(false)
    val isCircuitActive: StateFlow<Boolean> = _isCircuitActive.asStateFlow()

    private val _actions = MutableStateFlow<List<CircuitAction>>(emptyList())
    val actions: StateFlow<List<CircuitAction>> = _actions.asStateFlow()

    private val _stats = MutableStateFlow(CircuitStats())
    val stats: StateFlow<CircuitStats> = _stats.asStateFlow()

    private val _sensorDistanceCm = MutableStateFlow(0)

    private var firebaseDb: DatabaseReference? = null
    private var isFirebaseAvailable = false

    // Respaldo en memoria: solo para los usuarios de demostración sin conexión
    private val localUsers = mutableMapOf<String, User>()
    private val localActions = mutableListOf<CircuitAction>()

    init {
        initDemoUsers()
        setupFirebase()
        calculateStats()
    }

    private fun crearUsuario(
        rut: String, nombre: String, apellido: String,
        correo: String, cargo: String, clave: String
    ): User {
        val salt = PasswordHasher.newSalt()
        return User(
            rut = rut,
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            cargo = cargo,
            passwordHash = PasswordHasher.hash(clave, salt),
            salt = salt
        )
    }

    private fun initDemoUsers() {
        val demo = listOf(
            crearUsuario("11.111.111-1", "Juan", "Pérez", "juan.perez@inacapmail.cl", "Técnico IoT", "123456"),
            crearUsuario("22.222.222-2", "María", "González", "maria.gonzalez@inacapmail.cl", "Ingeniero IoT", "123456")
        )
        demo.forEach { localUsers[it.cleanRut] = it }
    }

    private fun setupFirebase() {
        try {
            val db = try {
                FirebaseDatabase.getInstance("https://pulsoiot-dbc34-default-rtdb.firebaseio.com")
            } catch (e: Exception) {
                FirebaseDatabase.getInstance()
            }
            val ref = db.reference
            firebaseDb = ref
            isFirebaseAvailable = true
            Log.d(TAG, "Firebase Realtime Database inicializada")

            // Crea los usuarios de demostración solo si no existen (no sobrescribe cuentas reales)
            for (user in localUsers.values) {
                val userRef = ref.child("users").child(user.cleanRut)
                userRef.get().addOnSuccessListener { snap ->
                    if (!snap.exists()) {
                        userRef.setValue(user)
                    }
                }
            }

            ref.child("actions").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lista = snapshot.children
                        .mapNotNull { it.getValue(CircuitAction::class.java) }
                        .sortedByDescending { it.timestamp }
                    _actions.value = lista
                    calculateStats()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error al leer acciones: ${error.message}")
                }
            })

            ref.child("circuit").child("active").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _isCircuitActive.value = snapshot.getValue(Boolean::class.java) ?: false
                    calculateStats()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error al leer circuito: ${error.message}")
                }
            })

            ref.child("sensors").child("distanceCm").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _sensorDistanceCm.value = snapshot.getValue(Long::class.java)?.toInt() ?: 0
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Error al leer sensor: ${error.message}")
                }
            })
        } catch (e: Exception) {
            Log.w(TAG, "Firebase no configurado o sin conexión. Modo de respaldo local.", e)
            isFirebaseAvailable = false
        }
    }

    private fun calculateStats() {
        val acciones = _actions.value
        _stats.value = CircuitStats(
            activaciones = acciones.count { it.accion == ACCION_ACTIVAR },
            desactivaciones = acciones.count { it.accion == ACCION_DESACTIVAR },
            obstaculosDetectados = acciones.count { it.accion == ACCION_OBSTACULO },
            distanciaTotalCm = acciones.sumOf { it.distanciaCm },
            isCircuitActive = _isCircuitActive.value
        )
    }

    fun login(rutInput: String, contrasenaInput: String, onResult: (Boolean, String?) -> Unit) {
        if (!RutUtils.isValidFormat(rutInput)) {
            onResult(false, "Ingresa un RUT válido.")
            return
        }
        val clean = RutUtils.clean(rutInput)
        val db = firebaseDb

        if (isFirebaseAvailable && db != null) {
            db.child("users").child(clean).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        verificarYEntrar(user, contrasenaInput, onResult)
                    } else {
                        checkLocalLogin(clean, contrasenaInput, onResult)
                    }
                }
                .addOnFailureListener {
                    checkLocalLogin(clean, contrasenaInput, onResult)
                }
        } else {
            checkLocalLogin(clean, contrasenaInput, onResult)
        }
    }

    private fun verificarYEntrar(user: User, clave: String, onResult: (Boolean, String?) -> Unit) {
        val valida = user.salt.isNotBlank() && user.passwordHash.isNotBlank() &&
                PasswordHasher.verify(clave, user.salt, user.passwordHash)
        if (valida) {
            _currentUser.value = user
            onResult(true, null)
        } else {
            // Mismo mensaje para RUT inexistente y contraseña incorrecta
            onResult(false, "RUT o contraseña incorrectos.")
        }
    }

    private fun checkLocalLogin(clean: String, clave: String, onResult: (Boolean, String?) -> Unit) {
        val user = localUsers[clean]
        if (user != null) {
            verificarYEntrar(user, clave, onResult)
        } else {
            onResult(false, "RUT o contraseña incorrectos.")
        }
    }

    fun register(user: User, clave: String, onResult: (Boolean, String?) -> Unit) {
        if (!RutUtils.isValidFormat(user.rut)) {
            onResult(false, "El RUT ingresado no es válido.")
            return
        }
        if (clave.length < MIN_CLAVE) {
            onResult(false, "La contraseña debe tener al menos $MIN_CLAVE caracteres.")
            return
        }
        val db = firebaseDb
        if (!isFirebaseAvailable || db == null) {
            onResult(false, "Sin conexión con la base de datos. Intenta más tarde.")
            return
        }

        val clean = user.cleanRut
        val ref = db.child("users").child(clean)
        ref.get()
            .addOnSuccessListener { snap ->
                if (snap.exists()) {
                    onResult(false, "Ya existe una cuenta con ese RUT.")
                    return@addOnSuccessListener
                }
                val salt = PasswordHasher.newSalt()
                val nuevo = user.copy(
                    passwordHash = PasswordHasher.hash(clave, salt),
                    salt = salt
                )
                ref.setValue(nuevo)
                    .addOnSuccessListener {
                        // Deja al usuario recién creado en sesión, igual que en el login
                        _currentUser.value = nuevo
                        onResult(true, null)
                    }
                    .addOnFailureListener { e ->
                        onResult(false, "No se pudo guardar la cuenta: ${e.localizedMessage}")
                    }
            }
            .addOnFailureListener { e ->
                onResult(false, "No se pudo verificar el RUT: ${e.localizedMessage}")
            }
    }

    fun toggleCircuit(setActive: Boolean, onResult: (Boolean) -> Unit) {
        val rut = _currentUser.value?.rut
        if (rut == null) {
            onResult(false)
            return
        }

        val now = Date()
        val fecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(now)
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(now)
        val accion = if (setActive) ACCION_ACTIVAR else ACCION_DESACTIVAR
        val distancia = _sensorDistanceCm.value

        val db = firebaseDb
        if (isFirebaseAvailable && db != null) {
            val ref = db.child("actions").push()
            val registro = CircuitAction(
                id = ref.key ?: "act_${now.time}",
                fecha = fecha,
                hora = hora,
                usuarioRut = rut,
                accion = accion,
                distanciaCm = distancia,
                timestamp = now.time
            )
            db.child("circuit").child("active").setValue(setActive)
            ref.setValue(registro)
                .addOnFailureListener { Log.w(TAG, "No se guardó la acción: ${it.message}") }
            onResult(true)
        } else {
            // Modo de respaldo local
            _isCircuitActive.value = setActive
            localActions.add(
                0,
                CircuitAction(
                    id = "act_${now.time}",
                    fecha = fecha,
                    hora = hora,
                    usuarioRut = rut,
                    accion = accion,
                    distanciaCm = distancia,
                    timestamp = now.time
                )
            )
            _actions.value = localActions.toList()
            calculateStats()
            onResult(true)
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    companion object {
        private const val TAG = "AppRepository"
        private const val MIN_CLAVE = 6

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(context: Context): AppRepository {
            return instance ?: synchronized(this) {
                instance ?: AppRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}
