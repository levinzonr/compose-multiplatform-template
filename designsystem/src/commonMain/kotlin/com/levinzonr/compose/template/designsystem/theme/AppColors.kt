package com.levinzonr.compose.template.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** Semantic colors shared by all features. Material 3 supplies the initial palettes. */
@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val error: Color,
    val onError: Color
)

internal val LightAppColors = lightColorScheme().toAppColors()
internal val DarkAppColors = darkColorScheme().toAppColors()

private fun ColorScheme.toAppColors() = AppColors(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    outline = outline,
    error = error,
    onError = onError
)

internal fun AppColors.toMaterialColors(darkTheme: Boolean): ColorScheme {
    val base = if (darkTheme) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        error = error,
        onError = onError
    )
}
