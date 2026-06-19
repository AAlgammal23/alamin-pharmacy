package com.alamin.pharma.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = TurquoiseDeep,
    onPrimary = White,
    primaryContainer = TurquoiseLight,
    onPrimaryContainer = InkDark,
    secondary = Mint,
    onSecondary = InkDark,
    background = White,
    onBackground = InkDark,
    surface = White,
    onSurface = InkDark,
    surfaceVariant = SurfacePale,
    onSurfaceVariant = InkSoft,
    outline = Outline,
    error = Error,
    onError = White,
    tertiary = Warning
)

private val DarkColors = darkColorScheme(
    primary = TurquoiseLight,
    onPrimary = InkDark,
    primaryContainer = TurquoiseDeep,
    onPrimaryContainer = White,
    secondary = Mint,
    onSecondary = InkDark,
    background = SurfacePale,
    onBackground = InkDark,
    surface = White,
    onSurface = InkDark,
    surfaceVariant = SurfacePale,
    onSurfaceVariant = InkSoft,
    outline = Outline,
    error = Error,
    onError = White,
    tertiary = Warning
)

private val ArTypography = Typography(
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleSmall = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
)

@Composable
fun AlAminTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = ArTypography,
        content = content
    )
}
