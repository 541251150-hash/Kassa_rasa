package com.kassarasa.pos.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KassaRasaLightColorScheme = lightColorScheme(
    primary = OliveDark,
    onPrimary = Color.White,
    primaryContainer = StatusGreenBg,
    onPrimaryContainer = OliveDark,
    secondary = EspressoDark,
    onSecondary = Color.White,
    secondaryContainer = PaymentTotalBox,
    onSecondaryContainer = TextHeadingBrown,
    tertiary = LogoRasaBrown,
    onTertiary = Color.White,
    background = CreamDashboardBg,
    onBackground = TextPrimaryDark,
    surface = CardSurfaceWarm,
    onSurface = TextPrimaryDark,
    surfaceVariant = InputFillWarm,
    onSurfaceVariant = TextSecondaryWarm,
    outline = BorderCardSubtle,
    error = StatusRedText,
    onError = Color.White
)

@Composable
fun KassaRasaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KassaRasaLightColorScheme,
        typography = KassaRasaTypography,
        content = content
    )
}
