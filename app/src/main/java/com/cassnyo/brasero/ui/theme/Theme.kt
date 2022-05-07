package com.cassnyo.brasero.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = ColorPrimary,
    primaryVariant = ColorPrimaryVariant,
    secondary = ColorSecondary,
    secondaryVariant = ColorSecondaryVariant,
    background = ColorBackground,
    surface = ColorSurface,
    error = ColorError,
    onPrimary = ColorOnPrimary,
    onSecondary = ColorOnSecondary,
    onBackground = ColorOnBackground,
    onSurface = ColorOnSurface,
    onError = ColorOnError
)

@Composable
fun BraseroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}