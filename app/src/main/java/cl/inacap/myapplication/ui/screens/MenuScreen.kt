package cl.inacap.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import cl.inacap.myapplication.ui.theme.EmeraldGreen
import cl.inacap.myapplication.ui.theme.RedCrimsonBright
import cl.inacap.myapplication.ui.theme.TextPrimary
import cl.inacap.myapplication.ui.theme.TextSecondary

@Composable
fun MenuScreen(
    repository: AppRepository,
    onNavigateToCircuit: () -> Unit,
    onNavigateToActions: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val isCircuitActive by repository.isCircuitActive.collectAsState()

    val userName = currentUser?.nombreCompleto ?: "Nombre Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        // Header displaying Nombre Usuario and Menú
        IotScreenHeader(
            userName = userName,
            title = "Menú"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Circuit Status Live Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = DarkCardSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (isCircuitActive) EmeraldGreen else RedCrimsonBright)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Estado del Circuito",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = if (isCircuitActive) "CIRCUITO ACTIVADO" else "CIRCUITO DESACTIVADO",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCircuitActive) EmeraldGreen else RedCrimsonBright
                            )
                        }
                    }
                }

                // Menu Options Buttons
                MenuButtonOption(
                    title = "Manipular Circuito",
                    subtitle = "Encender o apagar el circuito IoT",
                    icon = Icons.Default.PowerSettingsNew,
                    iconTint = CyberCyan,
                    onClick = onNavigateToCircuit
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuButtonOption(
                    title = "Ver Acciones Realizadas",
                    subtitle = "Historial de obstáculos y movimientos",
                    icon = Icons.AutoMirrored.Filled.ListAlt,
                    iconTint = CyberCyan,
                    onClick = onNavigateToActions
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuButtonOption(
                    title = "Ver Estadísticas",
                    subtitle = "Métricas de activaciones y sensor",
                    icon = Icons.Default.BarChart,
                    iconTint = CyberCyan,
                    onClick = onNavigateToStats
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuButtonOption(
                    title = "Ver Perfil",
                    subtitle = "Datos del usuario actual ($userName)",
                    icon = Icons.Default.AccountBox,
                    iconTint = CyberCyan,
                    onClick = onNavigateToProfile
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cerrar Sesion Button (Red button as requested in wireframe)
                RedButton(
                    text = "Cerrar Sesión",
                    onClick = {
                        repository.logout()
                        onLogout()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\"Menú\" debe mostrar nombre de usuario logueado y redireccionar.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MenuButtonOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
