package cl.arriagada.microsaasadministrator.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.inventory.presentation.theme.AppTypography


/**
 * InventoryTheme
 *
 * Tema Material Design 3 para la aplicación Inventario
 * Soporta light/dark mode y dynamic colors (Android 12+)
 *
 * Uso en MainActivity:
 * InventoryTheme {
 *     // Tu contenido aquí
 *     Surface { ... }
 * }
 */

// Esquema de colores para modo OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
)

// Esquema de colores para modo CLARO
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
)

/**
 * InventoryTheme Composable
 *
 * @param darkTheme: Boolean - Si verdadero, usa colores oscuros. Por defecto detecta automáticamente.
 * @param dynamicColor: Boolean - Si verdadero (y Android 12+), usa colores dinámicos del sistema.
 * @param content: El contenido a mostrar dentro del tema
 */
@Composable
fun InventoryTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // 1. Seleccionar esquema de colores según versión de Android y preferencias
    val colorScheme = when {
        // Android 12+ (API 31+): Soporta colores dinámicos del sistema
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        // Android < 12: Usar colores predefinidos
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 2. Configurar status bar y navigation bar (solo si no estamos en preview)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()

            // Configurar contraste de iconos en status bar (API 26+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val insetsController = view.windowInsetsController
                insetsController?.isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    // 3. Aplicar tema Material Design 3
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,    // Definido en Type.kt
        shapes = AppShapes,            // Definido en Shape.kt
        content = content
    )
}