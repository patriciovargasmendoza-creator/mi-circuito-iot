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
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
fun StatisticsScreen(
    repository: AppRepository,
    onBackClick: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val stats by repository.stats.collectAsState()
    val userName = currentUser?.nombreCompleto ?: "Nombre Usuario"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        // Header showing Nombre Usuario and Estadísticas
        IotScreenHeader(
            userName = userName,
            title = "Estadísticas",
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Métricas Generales del Circuito IoT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan,
                    modifier = Modifier.padding(bottom = 16.dp, top = 4.dp)
                )

                // Wireframe Metrics Card Grid
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Metric 1: Activaciones
                        StatMetricRow(
                            label = "Activaciones",
                            value = "${stats.activaciones}",
                            icon = Icons.Default.PowerSettingsNew,
                            color = EmeraldGreen,
                            unit = "veces"
                        )

                        // Metric 2: Desactivaciones
                        StatMetricRow(
                            label = "Desactivaciones",
                            value = "${stats.desactivaciones}",
                            icon = Icons.Default.PowerOff,
                            color = RedCrimsonBright,
                            unit = "veces"
                        )

                        // Metric 3: Obstáculos Detectados
                        StatMetricRow(
                            label = "Obstáculos Detectados",
                            value = "${stats.obstaculosDetectados}",
                            icon = Icons.Default.Radar,
                            color = CyberCyan,
                            unit = "eventos"
                        )

                        // Metric 4: Distancia Total
                        StatMetricRow(
                            label = "Distancia Total",
                            value = "${stats.distanciaTotalCm} cm",
                            icon = Icons.Default.Straighten,
                            color = CyberBlue,
                            unit = "acumulados"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Visual Chart/Gauge Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardSurface)
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
                            Text(
                                text = "Proporción de Uso",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Icon(
                                imageVector = Icons.Default.ElectricalServices,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val totalOps = (stats.activaciones + stats.desactivaciones).coerceAtLeast(1)
                        val activeProgress = stats.activaciones.toFloat() / totalOps.toFloat()

                        LinearProgressIndicator(
                            progress = { activeProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = EmeraldGreen,
                            trackColor = RedCrimsonBright
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Activo: ${(activeProgress * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Inactivo: ${((1f - activeProgress) * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = RedCrimsonBright,
                                fontWeight = FontWeight.Bold
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
                    text = "\"Estadísticas\" debe mostrar nombre de usuario logueado y ejemplo de cantidades, según circuito y sensor implementado.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StatMetricRow(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    unit: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = unit,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}
