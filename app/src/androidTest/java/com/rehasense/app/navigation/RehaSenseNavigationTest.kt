package com.rehasense.app.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rehasense.app.MainActivity
import com.rehasense.app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RehaSenseNavigationTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    private fun node(id: Int) = compose.onNodeWithText(compose.activity.getString(id))

    private fun selectProfileScreen() {
        node(R.string.nav_start).performScrollTo().performClick()
        node(R.string.nav_select_title).assertIsDisplayed()
    }

    @Test
    fun welcomeOpensProfileSelection() {
        node(R.string.nav_welcome_title).assertIsDisplayed()
        selectProfileScreen()
        node(R.string.nav_continue).performScrollTo().assertIsNotEnabled()
    }

    @Test
    fun patientSelectionOpensPatientHome() {
        selectProfileScreen()
        node(R.string.nav_patient).performScrollTo().performClick()
        node(R.string.nav_continue).performScrollTo().assertIsEnabled().performClick()
        node(R.string.nav_patient_home).assertIsDisplayed()
    }

    @Test
    fun physiotherapistSelectionOpensProfessionalHome() {
        selectProfileScreen()
        node(R.string.nav_physiotherapist).performScrollTo().performClick()
        node(R.string.nav_continue).performScrollTo().performClick()
        node(R.string.nav_physiotherapist_home).assertIsDisplayed()
    }

    @Test
    fun changeProfileReturnsToSelectionAndAllowsDifferentRole() {
        selectProfileScreen()
        node(R.string.nav_patient).performScrollTo().performClick()
        node(R.string.nav_continue).performScrollTo().performClick()
        node(R.string.nav_change_profile).performScrollTo().performClick()
        node(R.string.nav_physiotherapist).performScrollTo().performClick()
        node(R.string.nav_continue).performScrollTo().performClick()
        node(R.string.nav_physiotherapist_home).assertIsDisplayed()
    }

    @Test
    fun backButtonReturnsToWelcome() {
        selectProfileScreen()
        node(R.string.nav_back).performScrollTo().performClick()
        node(R.string.nav_welcome_title).assertIsDisplayed()
    }
}
