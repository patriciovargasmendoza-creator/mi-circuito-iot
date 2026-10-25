package cl.inacap.myapplication.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.inacap.myapplication.data.repository.AppRepository
import cl.inacap.myapplication.ui.components.IotScreenHeader
import cl.inacap.myapplication.ui.components.RedButton
import cl.inacap.myapplication.ui.theme.DarkBorder
import cl.inacap.myapplication.ui.theme.DarkCardSurface
import cl.inacap.myapplication.ui.theme.DarkSlateBackground
import cl.inacap.myapplication.ui.theme.DarkSurfaceVariant
import cl.inacap.myapplication.ui.theme.EmeraldDark
import cl.inacap.myapplication.ui.theme.EmeraldGreen
import cl.inacap.myapplication.ui.theme.RedCrimson
import cl.inacap.myapplication.ui.theme.RedCrimsonBright
import cl.inacap.myapplication.ui.theme.TextPrimary
import cl.inacap.myapplication.ui.theme.TextSecondary

@Composable
fun CircuitControlScreen(
    repository: AppRepository,
    onBackClick: () -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val isCircuitActive by repository.isCircuitActive.collectAsState()
    val userName = currentUser?.nombreCompleto ?: "Nombre Usuario"

    var feedbackMessage by remember { mutableStateOf<String?>(null) }

    val statusColor by animateColorAsState(
        targetValue = if (isCircuitActive) EmeraldGreen else RedCrimsonBright,
        animationSpec = tween(400),
        label = "statusColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBackground)
    ) {
        // Header with logged in user name and screen title
        IotScreenHeader(
            userName = userName,
            title = "Manipular Circuito",
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

                // Circuit Live Indicator Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = DarkCardSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(statusColor.copy(alpha = 0.2f))
                                .border(2.dp, statusColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCircuitActive) Icons.Default.PowerSettingsNew else Icons.Default.PowerOff,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isCircuitActive) "ESTADO: ENERGIZADO / ACTIVO" else "ESTADO: DESCONECTADO",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColor
                        )

                        Text(
                            text = if (isCircuitActive) "Monitoreando sensores en tiempo real" else "Presiona 'Activar' para energizar el circuito",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Circle Control Buttons as drawn in wireframe
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circle Button "Activar"
                    CircleControlButton(
                        text = "Activar",
                        isPrimary = true,
                        isSelected = isCircuitActive,
                        onClick = {
                            repository.toggleCircuit(true) { success ->
                                feedbackMessage = if (success) "¡Circuito Activado exitosamente!" else "No se pudo cambiar el circuito. Revisa tu sesión y la conexión."
                            }
                        }
                    )

                    // Circle Button "Desactivar"
                    CircleControlButton(
                        text = "Desactivar",
                        isPrimary = false,
                        isSelected = !isCircuitActive,
                        onClick = {
                            repository.toggleCircuit(false) { success ->
                                feedbackMessage = if (success) "Circuito Desactivado." else "No se pudo cambiar el circuito. Revisa tu sesión y la conexión."
                            }
                        }
                    )
                }

                if (feedbackMessage != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feedbackMessage ?: "",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
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
                    text = "\"Manipular Circuito\" debe mostrar nombre de usuario logueado y redireccionar.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun CircleControlButton(
    text: String,
    isPrimary: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "scale")

    val bgBrush = if (isPrimary) {
        Brush.radialGradient(listOf(EmeraldGreen, EmeraldDark))
    } else {
        Brush.radialGradient(listOf(RedCrimsonBright, RedCrimson))
    }

    val glowColor = if (isPrimary) EmeraldGreen else RedCrimsonBright

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
                .background(bgBrush)
                .border(
                    width = if (isSelected) 4.dp else 2.dp,
                    color = if (isSelected) Color.White else glowColor.copy(alpha = 0.5f),
                    shape = CircleShape
                )
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (isPrimary) Icons.Default.PowerSettingsNew else Icons.Default.PowerOff,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
