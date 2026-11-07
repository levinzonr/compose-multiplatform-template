package com.levinzonr.compose.template.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAppColors = staticCompositionLocalOf { LightAppColors }
private val LocalAppDimensions = staticCompositionLocalOf { AppDimensions() }

/** The only theme entry point used by app and feature UI. */
object AppTheme {
    val colors: AppColors
        @Composable @ReadOnlyComposable
        get() = LocalAppColors.current

    val dimensions: AppDimensions
        @Composable @ReadOnlyComposable
        get() = LocalAppDimensions.current

    val typography: Typography
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.shapes
}

/** Overrides are useful for alternate brands, nested themes, and deterministic previews. */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: AppColors = if (darkTheme) DarkAppColors else LightAppColors,
    dimensions: AppDimensions = AppDimensions(),
    typography: Typography = DefaultAppTypography,
    shapes: Shapes = DefaultAppShapes,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppDimensions provides dimensions
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColors(darkTheme),
            typography = typography,
            shapes = shapes
        ) {
            Surface(
                color = colors.background,
                contentColor = colors.onBackground,
                content = content
            )
        }
    }
}
