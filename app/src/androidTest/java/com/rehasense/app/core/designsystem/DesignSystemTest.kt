package com.rehasense.app.core.designsystem

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rehasense.app.core.designsystem.components.RehaSenseButton
import com.rehasense.app.core.designsystem.components.RehaSenseCard
import com.rehasense.app.core.designsystem.theme.RehaSenseTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DesignSystemTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun lightThemeUsesBrandColorsAndReadableText() = assertPalette(
        dark = false,
        expectedPrimary = Color(0xFF0369A1),
        expectedBackground = Color(0xFFF5F9FC)
    )

    @Test
    fun darkThemeUsesBrandColorsAndReadableText() = assertPalette(
        dark = true,
        expectedPrimary = Color(0xFF8FD3FF),
        expectedBackground = Color(0xFF101C25)
    )

    @Test
    fun enabledButtonCallsCallback() {
        var clicks = 0
        compose.setContent {
            RehaSenseTheme {
                RehaSenseButton(text = "Continuar", onClick = { clicks++ })
            }
        }
        compose.onNodeWithText("Continuar").assertHasClickAction().performClick()
        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun disabledButtonIsNotEnabled() {
        compose.setContent {
            RehaSenseTheme {
                RehaSenseButton(text = "No disponible", onClick = {}, enabled = false)
            }
        }
        compose.onNodeWithText("No disponible").assertIsNotEnabled()
    }

    @Test
    fun cardDisplaysItsContent() {
        compose.setContent {
            RehaSenseTheme {
                RehaSenseCard { Text("Contenido") }
            }
        }
        compose.onNodeWithText("Contenido").assertIsDisplayed()
    }

    private fun assertPalette(dark: Boolean, expectedPrimary: Color, expectedBackground: Color) {
        var actual: ColorScheme? = null
        compose.setContent {
            RehaSenseTheme(darkTheme = dark) {
                val colors = MaterialTheme.colorScheme
                SideEffect { actual = colors }
                Text("RehaSense")
            }
        }
        compose.runOnIdle {
            val colors = requireNotNull(actual)
            assertEquals(expectedPrimary, colors.primary)
            assertEquals(expectedBackground, colors.background)
            assertContrast(colors.primary, colors.onPrimary)
            assertContrast(colors.primaryContainer, colors.onPrimaryContainer)
            assertContrast(colors.background, colors.onBackground)
            assertContrast(colors.surface, colors.onSurface)
            assertContrast(colors.surfaceVariant, colors.onSurfaceVariant)
            assertContrast(colors.error, colors.onError)
        }
    }

    private fun assertContrast(background: Color, foreground: Color) {
        val a = background.luminance().toDouble()
        val b = foreground.luminance().toDouble()
        val ratio = (maxOf(a, b) + 0.05) / (minOf(a, b) + 0.05)
        assertTrue("Contrast must be at least 4.5:1; actual: $ratio", ratio >= 4.5)
    }
}
