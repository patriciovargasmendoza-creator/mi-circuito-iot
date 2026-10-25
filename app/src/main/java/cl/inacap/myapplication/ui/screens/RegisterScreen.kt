package cl.inacap.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.inacap.myapplication.data.model.User
import cl.inacap.myapplication.data.repository.AppRepository
import cl.inacap.myapplication.ui.components.CyberGradientButton
import cl.inacap.myapplication.ui.components.RedButton
import cl.inacap.myapplication.ui.theme.CyberCyan
import cl.inacap.myapplication.ui.theme.DarkBorder
import cl.inacap.myapplication.ui.theme.DarkCardSurface
import cl.inacap.myapplication.ui.theme.DarkSlateBackground
import cl.inacap.myapplication.ui.theme.DarkSurfaceVariant
import cl.inacap.myapplication.ui.theme.RedCrimsonBright
import cl.inacap.myapplication.ui.theme.TextPrimary
import cl.inacap.myapplication.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    repository: AppRepository,
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("Operador IoT") }
    var contrasena by remember { mutableStateOf("") }
    var repetirContrasena by remember { mutableStateOf("") }

    var cargoExpanded by remember { mutableStateOf(false) }
    val cargoOptions = listOf("Operador IoT", "Técnico IoT", "Ingeniero IoT", "Administrador", "Supervisor")

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CyberCyan,
        unfocusedBorderColor = DarkBorder,
        focusedLabelColor = CyberCyan,
        unfocusedLabelColor = TextSecondary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        focusedContainerColor = DarkSurfaceVariant,
        unfocusedContainerColor = DarkSurfaceVariant
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear Cuenta",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Text(
                text = "Registro de Usuario en Firebase Realtime Database",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Form Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                color = DarkCardSurface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Rut
                    OutlinedTextField(
                        value = rut,
                        onValueChange = {
                            rut = it
                            errorMessage = null
                        },
                        label = { Text("Rut") },
                        placeholder = { Text("11.111.111-1") },
                        leadingIcon = { Icon(Icons.Default.Badge, null, tint = CyberCyan) },
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre & Apellido
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {
                                nombre = it
                                errorMessage = null
                            },
                            label = { Text("Nombre") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = CyberCyan) },
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = apellido,
                            onValueChange = {
                                apellido = it
                                errorMessage = null
                            },
                            label = { Text("Apellido") },
                            singleLine = true,
                            colors = fieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Correo Electrónico
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            errorMessage = null
                        },
                        label = { Text("Correo Electrónico") },
                        placeholder = { Text("usuario@inacapmail.cl") },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = CyberCyan) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cargo Dropdown
                    ExposedDropdownMenuBox(
                        expanded = cargoExpanded,
                        onExpandedChange = { cargoExpanded = !cargoExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cargo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Cargo") },
                            leadingIcon = { Icon(Icons.Default.Work, null, tint = CyberCyan) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cargoExpanded) },
                            colors = fieldColors,
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = cargoExpanded,
                            onDismissRequest = { cargoExpanded = false },
                            modifier = Modifier.background(DarkCardSurface)
                        ) {
                            cargoOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = TextPrimary) },
                                    onClick = {
                                        cargo = option
                                        cargoExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Contraseña
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            errorMessage = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = CyberCyan) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Repetir Contraseña
                    OutlinedTextField(
                        value = repetirContrasena,
                        onValueChange = {
                            repetirContrasena = it
                            errorMessage = null
                        },
                        label = { Text("Repetir Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = CyberCyan) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = RedCrimsonBright,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons Row (Registrar and Volver)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CyberGradientButton(
                            text = if (isLoading) "Guardando..." else "Registrar",
                            onClick = {
                                if (rut.isBlank() || nombre.isBlank() || apellido.isBlank() || contrasena.isBlank()) {
                                    errorMessage = "Por favor completa todos los campos requeridos."
                                    return@CyberGradientButton
                                }
                                if (contrasena != repetirContrasena) {
                                    errorMessage = "Las contraseñas no coinciden."
                                    return@CyberGradientButton
                                }
                                isLoading = true
                                val newUser = User(
                                    rut = rut.trim(),
                                    nombre = nombre.trim(),
                                    apellido = apellido.trim(),
                                    correo = correo.trim(),
                                    cargo = cargo
                                )
                                repository.register(newUser, contrasena) { success, msg ->
                                    isLoading = false
                                    if (success) {
                                        onRegisterSuccess()
                                    } else {
                                        errorMessage = msg ?: "Error al registrar el usuario."
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        )

                        RedButton(
                            text = "Volver",
                            onClick = onBackClick,
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "\"Crear cuenta\" debe registrar en la colección de la base de datos en Firebase.",
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CyberCyan)
            }
        }
    }
}
