package cl.inacap.myapplication.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.inacap.myapplication.data.repository.AppRepository
import cl.inacap.myapplication.ui.components.CyberGradientButton
import cl.inacap.myapplication.util.RutUtils
import cl.inacap.myapplication.ui.theme.CyberBlue
import cl.inacap.myapplication.ui.theme.CyberCyan
import cl.inacap.myapplication.ui.theme.DarkBorder
import cl.inacap.myapplication.ui.theme.DarkCardSurface
import cl.inacap.myapplication.ui.theme.DarkSlateBackground
import cl.inacap.myapplication.ui.theme.DarkSurfaceVariant
import cl.inacap.myapplication.ui.theme.EmeraldGreen
import cl.inacap.myapplication.ui.theme.RedCrimsonBright
import cl.inacap.myapplication.ui.theme.TextPrimary
import cl.inacap.myapplication.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    repository: AppRepository,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var rut by remember { mutableStateOf(TextFieldValue("")) }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header IoT Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(CyberCyan.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                    .border(2.dp, CyberCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = "IoT Logo",
                    tint = CyberCyan,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Text(
                text = "Control de Circuito IoT - Realtime Database",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
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
                        .padding(24.dp)
                ) {
                    // Rut Field
                    OutlinedTextField(
                        value = rut,
                        onValueChange = { input ->
                            val formateado = RutUtils.format(input.text)
                            val validosAntes = RutUtils.contarValidos(input.text.take(input.selection.end))
                            val cursor = RutUtils.cursorTras(formateado, validosAntes)
                            rut = TextFieldValue(formateado, TextRange(cursor))
                            errorMessage = null
                        },
                        label = { Text("RUT") },
                        placeholder = { Text("Ej: 12.345.678-K") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = null,
                                tint = CyberCyan
                            )
                        },
                        trailingIcon = {
                            if (rut.text.isNotEmpty()) {
                                IconButton(onClick = { rut = TextFieldValue("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar RUT",
                                        tint = TextSecondary
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextSecondary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña Field
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            errorMessage = null
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = CyberCyan
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Ver contraseña",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = DarkBorder,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextSecondary,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
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

                    Spacer(modifier = Modifier.height(28.dp))

                    // Iniciar Sesion Button
                    CyberGradientButton(
                        text = if (isLoading) "Verificando..." else "Iniciar Sesión",
                        onClick = {
                            if (rut.text.isBlank() || contrasena.isBlank()) {
                                errorMessage = "Por favor ingresa Rut y Contraseña."
                                return@CyberGradientButton
                            }
                            isLoading = true
                            errorMessage = null
                            repository.login(rut.text, contrasena) { success, msg ->
                                isLoading = false
                                if (success) {
                                    onLoginSuccess()
                                } else {
                                    errorMessage = msg ?: "Error al iniciar sesión."
                                }
                            }
                        },
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Crear Cuenta Button
                    OutlinedButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        border = BorderStroke(1.5.dp, CyberCyan)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Crear Cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Demo Fill Pill for instant testing
            Surface(
                onClick = {
                    rut = TextFieldValue("11.111.111-1")
                    contrasena = "123456"
                    errorMessage = null
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .border(1.dp, EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(30.dp)),
                color = DarkCardSurface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Probar Demo (Rut: 11.111.111-1)",
                        color = EmeraldGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "El Login debe ser con Firebase (Realtime Database), es decir, con usuarios existentes en la Base de Datos.",
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
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

private fun formatRutInput(input: String): String {
    val clean = input.replace(".", "").replace("-", "").uppercase()
    if (clean.isEmpty()) return ""
    if (clean.length == 1) return clean

    val body = if (clean.length > 1) clean.substring(0, clean.length - 1) else ""
    val dv = clean.takeLast(1)

    val formattedBody = StringBuilder()
    var count = 0
    for (i in body.length - 1 downTo 0) {
        formattedBody.append(body[i])
        count++
        if (count == 3 && i != 0) {
            formattedBody.append(".")
            count = 0
        }
    }
    val resultBody = formattedBody.reverse().toString()
    return "$resultBody-$dv"
}
