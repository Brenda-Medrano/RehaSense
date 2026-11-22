package com.rehasense.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = ActionBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DeepBlue,
    secondary = SecondaryText,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAF3),
    onSecondaryContainer = LightText,
    tertiary = DeepBlue,
    onTertiary = Color.White,
    tertiaryContainer = SoftBlue,
    onTertiaryContainer = DeepBlue,
    background = LightBackground,
    onBackground = LightText,
    surface = Color.White,
    onSurface = LightText,
    surfaceVariant = Color(0xFFE4EDF3),
    onSurfaceVariant = SecondaryText,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = LightBackground,
    surfaceContainer = Color(0xFFEDF4F9),
    surfaceContainerHigh = Color(0xFFE4EDF3),
    surfaceContainerHighest = Color(0xFFDBE8F0),
    surfaceTint = SkyBlue,
    inverseSurface = DarkSurface,
    inverseOnSurface = DarkText,
    inversePrimary = DarkPrimary,
    outline = Color(0xFF708390),
    outlineVariant = Color(0xFFCEDCE6),
    error = LightError,
    onError = Color.White,
    errorContainer = Color(0xFFFEE4E2),
    onErrorContainer = Color(0xFF7A1812)
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkBlueContainer,
    onPrimaryContainer = DarkOnBlueContainer,
    secondary = DarkSecondaryText,
    onSecondary = Color(0xFF22343F),
    secondaryContainer = Color(0xFF304653),
    onSecondaryContainer = DarkText,
    tertiary = Color(0xFFB1DDF7),
    onTertiary = DarkOnPrimary,
    tertiaryContainer = DarkBlueContainer,
    onTertiaryContainer = DarkOnBlueContainer,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = Color(0xFF30434F),
    onSurfaceVariant = DarkSecondaryText,
    surfaceContainerLowest = Color(0xFF0C1720),
    surfaceContainerLow = DarkBackground,
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = Color(0xFF223541),
    surfaceContainerHighest = Color(0xFF2C414E),
    surfaceTint = DarkPrimary,
    inverseSurface = LightBackground,
    inverseOnSurface = LightText,
    inversePrimary = ActionBlue,
    outline = Color(0xFF91A7B5),
    outlineVariant = Color(0xFF435866),
    error = DarkError,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF7A1812),
    onErrorContainer = Color(0xFFFFDAD6)
)

/** Uses the RehaSense palette consistently, including on Android 12 and newer. */
@Composable
fun RehaSenseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = RehaSenseTypography,
        shapes = RehaSenseShapes,
        content = content
    )
}
