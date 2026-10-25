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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Sensors
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
import cl.inacap.myapplication.data.model.CircuitAction
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
fun ActionListScreen(
    repository: AppRepository,
    onBackClick: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val actions by repository.actions.collectAsState()
    val userName = currentUser?.nombreCompleto ?: "Nombre Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        // Header showing Nombre Usuario and Ver Acciones
        IotScreenHeader(
            userName = userName,
            title = "Ver Acciones",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Historial Registrado (${actions.size} registros)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CyberCyan,
                modifier = Modifier.padding(bottom = 12.dp, top = 4.dp)
            )

            if (actions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay acciones registradas en el circuito.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(actions) { action ->
                        ActionDataCard(action = action)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Volver Button (Red button as requested in wireframe)
            RedButton(
                text = "Volver",
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "\"Ver Acciones\" debe mostrar nombre de usuario logueado e imagen con ejemplo de listado de datos.",
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun ActionDataCard(action: CircuitAction) {
    val isAlert = action.accion.lowercase().contains("obstáculo") || action.accion.lowercase().contains("impactar")
    val badgeColor = if (isAlert) RedCrimsonBright else EmeraldGreen

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isAlert) Icons.Default.Radar else Icons.Default.Sensors,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = action.accion,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Surface(
                    color = badgeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (action.distanciaCm > 0) "${action.distanciaCm} cm" else "N/A",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Data fields exactly matching wireframe layout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DataFieldRow(label = "Fecha", value = action.fecha, icon = Icons.Default.CalendarToday)
                DataFieldRow(label = "Hora", value = action.hora, icon = Icons.Default.AccessTime)
                DataFieldRow(label = "Usuario", value = action.usuarioRut, icon = Icons.Default.Person)
                DataFieldRow(label = "Acción", value = action.accion, icon = Icons.Default.Sensors)
                DataFieldRow(
                    label = "Distancia",
                    value = if (action.distanciaCm < 10) "0${action.distanciaCm} cm" else "${action.distanciaCm} cm",
                    icon = Icons.Default.Radar
                )
            }
        }
    }
}

@Composable
private fun DataFieldRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CyberCyan,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label : ",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
