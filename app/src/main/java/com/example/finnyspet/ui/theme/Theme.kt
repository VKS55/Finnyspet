package com.example.finnyspet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Scheme = darkColorScheme(
    primary = Purple,
    onPrimary = TextMain,
    background = Bg,
    onBackground = TextMain,
    surface = Card,
    onSurface = TextMain,
    secondary = Blue,
    tertiary = Green,
    error = Red
)

@Composable
fun FinnyspetTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = AppTypography,
        content = content
    )
}