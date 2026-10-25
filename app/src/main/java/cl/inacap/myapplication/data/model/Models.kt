package cl.inacap.myapplication.data.model

import cl.inacap.myapplication.util.RutUtils

// Tipos de acción usados por la interfaz, el repositorio y las estadísticas
const val ACCION_ACTIVAR = "Circuito Activado"
const val ACCION_DESACTIVAR = "Circuito Desactivado"
const val ACCION_OBSTACULO = "Obstáculo Detectado"

data class User(
    val rut: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val cargo: String = "",
    val passwordHash: String = "",
    val salt: String = ""
) {
    val cleanRut: String
        get() = RutUtils.clean(rut)

    val nombreCompleto: String
        get() = if (nombre.isBlank() && apellido.isBlank()) "Usuario" else "$nombre $apellido".trim()
}

data class CircuitAction(
    val id: String = "",
    val fecha: String = "",
    val hora: String = "",
    val usuarioRut: String = "",
    val accion: String = "",
    val distanciaCm: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class CircuitStats(
    val activaciones: Int = 0,
    val desactivaciones: Int = 0,
    val obstaculosDetectados: Int = 0,
    val distanciaTotalCm: Int = 0,
    val isCircuitActive: Boolean = false
)
