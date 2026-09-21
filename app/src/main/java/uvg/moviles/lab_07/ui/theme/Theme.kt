package uvg.moviles.lab_07.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = RickMortyBluePrimary,
    onPrimary = RickMortyOnPrimary,
    secondary = RickMortySecondary,
    background = RickMortyBackground,
    onBackground = RickMortyOnBackground,
    surface = RickMortySurface,
    onSurface = RickMortyOnSurface,
    primaryContainer = RickMortyBlueLight,
)

private val DarkColors = darkColorScheme(
    primary = RickMortyBlueDark,
    onPrimary = RickMortyOnPrimary,
    secondary = RickMortySecondary,
    background = RickMortyOnBackground,
    onBackground = RickMortyBackground,
    surface = RickMortyBlueDark,
    onSurface = RickMortyBackground,
)

@Composable
fun Lab07Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se deja apagado a propósito: queremos que la app use SIEMPRE la
    // paleta azul definida arriba a través de MaterialTheme, y no los
    // colores dinámicos del wallpaper del usuario (Android 12+).
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
