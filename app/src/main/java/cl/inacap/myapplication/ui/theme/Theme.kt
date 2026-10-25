package cl.inacap.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Color.Black,
    primaryContainer = CyberBlue,
    onPrimaryContainer = Color.White,
    secondary = EmeraldGreen,
    onSecondary = Color.Black,
    background = DarkSlateBackground,
    onBackground = TextPrimary,
    surface = DarkCardSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    error = RedCrimsonBright,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006689),
    onPrimary = Color.White,
    secondary = Color(0xFF006E3A),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    error = RedCrimson
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to dark IoT cyber theme for eye-catching visual
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
