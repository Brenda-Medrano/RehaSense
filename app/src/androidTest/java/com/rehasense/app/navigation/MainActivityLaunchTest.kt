package com.rehasense.app.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rehasense.app.MainActivity
import com.rehasense.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Separate smoke test: catches MainActivity still rendering an old placeholder. */
@RunWith(AndroidJUnit4::class)
class MainActivityLaunchTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun launchShowsRehaSenseWelcome() {
        compose.onNodeWithText(compose.activity.getString(R.string.nav_welcome_title))
            .assertIsDisplayed()
    }
}
