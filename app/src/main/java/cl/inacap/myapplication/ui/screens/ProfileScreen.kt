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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.inacap.myapplication.data.repository.AppRepository
import cl.inacap.myapplication.ui.components.IotScreenHeader
import cl.inacap.myapplication.ui.components.RedButton
import cl.inacap.myapplication.ui.theme.CyberCyan
import cl.inacap.myapplication.ui.theme.DarkBorder
import cl.inacap.myapplication.ui.theme.DarkCardSurface
import cl.inacap.myapplication.ui.theme.DarkSlateBackground
import cl.inacap.myapplication.ui.theme.DarkSurfaceVariant
import cl.inacap.myapplication.ui.theme.TextPrimary
import cl.inacap.myapplication.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    repository: AppRepository,
    onBackClick: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val user = currentUser

    val userName = user?.nombreCompleto ?: "Nombre Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        // Header showing Nombre Usuario and Ver Perfil
        IotScreenHeader(
            userName = userName,
            title = "Ver Perfil",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Profile Avatar Badge
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(2.dp, CyberCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = CyberCyan,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = user?.cargo ?: "Cargo Usuario",
                    fontSize = 13.sp,
                    color = CyberCyan,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Detailed Profile Data Card
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
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        ProfileDetailRow(
                            label = "Rut",
                            value = user?.rut ?: "No especificado",
                            icon = Icons.Default.Badge
                        )

                        ProfileDetailRow(
                            label = "Nombre",
                            value = user?.nombre ?: "No especificado",
                            icon = Icons.Default.Person
                        )

                        ProfileDetailRow(
                            label = "Apellido",
                            value = user?.apellido ?: "No especificado",
                            icon = Icons.Default.Person
                        )

                        ProfileDetailRow(
                            label = "Correo",
                            value = user?.correo ?: "No especificado",
                            icon = Icons.Default.Email
                        )

                        ProfileDetailRow(
                            label = "Cargo",
                            value = user?.cargo ?: "No especificado",
                            icon = Icons.Default.Work
                        )

                        // Password Row with Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Contraseña : ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                modifier = Modifier.width(100.dp)
                            )
                            Text(
                                text = "••••••••",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Volver Button (Red button as requested in wireframe)
                RedButton(
                    text = "Volver",
                    onClick = onBackClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\"Ver Perfil\" debe mostrar todos los datos del usuario con el cual se inicie la sesión.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CyberCyan,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label : ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
