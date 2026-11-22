package com.rehasense.app.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rehasense.app.R
import com.rehasense.app.core.designsystem.theme.RehaSenseTheme
import com.rehasense.app.domain.model.LocalUserProfile
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.repository.ProfileRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Every navigation test starts on Welcome and uses its own fake repository.
 * The Activity launch and the real Room implementation are covered separately.
 */
@RunWith(AndroidJUnit4::class)
class RehaSenseNavigationTest {
    @get:Rule val compose = createComposeRule()

    private val repository = FakeProfileRepository()

    @Before fun setUp() {
        compose.setContent {
            RehaSenseTheme {
                RehaSenseNavHost(profileRepository = repository)
            }
        }
    }

    private fun string(id: Int): String =
        InstrumentationRegistry.getInstrumentation().targetContext.getString(id)

    private fun node(id: Int) = compose.onNodeWithText(string(id))

    private fun button(tag: String) = compose.onNodeWithTag(tag)

    private fun chooseRole(tag: String) {
        button(tag).performScrollTo().performClick()
        button("nav_continue_button").performScrollTo().assertIsEnabled().performClick()
        node(R.string.profile_create_title).assertIsDisplayed()
    }

    private fun selectProfileScreen() {
        button("nav_start_button").performScrollTo().performClick()
        node(R.string.nav_select_title).assertIsDisplayed()
    }

    private fun createProfile(name: String, expectedTitle: Int) {
        compose.onNodeWithTag("profile_name_input").performScrollTo().performTextInput(name)
        button("profile_create_button").performScrollTo().performClick()
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText(string(expectedTitle)).fetchSemanticsNodes().isNotEmpty()
        }
        node(expectedTitle).assertIsDisplayed()
    }

    @Test fun welcomeOpensProfileSelection() {
        node(R.string.nav_welcome_title).assertIsDisplayed()
        selectProfileScreen()
        button("nav_continue_button").assertIsNotEnabled()
    }

    @Test fun patientSelectionOpensPatientHome() {
        selectProfileScreen()
        chooseRole("nav_role_patient")
        createProfile("Ana", R.string.nav_patient_home)
        compose.onNodeWithText(
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.profile_greeting, "Ana")
        ).assertIsDisplayed()
    }

    @Test fun physiotherapistSelectionOpensProfessionalHome() {
        selectProfileScreen()
        chooseRole("nav_role_physiotherapist")
        createProfile("Carlos", R.string.nav_physiotherapist_home)
    }

    @Test fun changeProfileReturnsToSelectionAndAllowsDifferentRole() {
        selectProfileScreen()
        chooseRole("nav_role_patient")
        createProfile("Ana", R.string.nav_patient_home)
        button("nav_change_profile_button").performScrollTo().performClick()
        node(R.string.nav_select_title).assertIsDisplayed()
        chooseRole("nav_role_physiotherapist")
        createProfile("Carlos", R.string.nav_physiotherapist_home)
    }

    @Test fun blankNameShowsValidationAndKeepsRegistrationOpen() {
        selectProfileScreen()
        chooseRole("nav_role_patient")
        button("profile_create_button").performScrollTo().performClick()
        node(R.string.profile_name_required).performScrollTo().assertIsDisplayed()
        node(R.string.profile_create_title).assertIsDisplayed()
    }

    @Test fun backButtonReturnsToWelcome() {
        selectProfileScreen()
        node(R.string.nav_back).performScrollTo().performClick()
        node(R.string.nav_welcome_title).assertIsDisplayed()
    }

    @Test fun savedPatientCanBeRestoredAfterChangingProfile() {
        selectProfileScreen()
        chooseRole("nav_role_patient")
        createProfile("Ana", R.string.nav_patient_home)
        button("nav_change_profile_button").performScrollTo().performClick()
        chooseRole("nav_role_patient")
        button("profile_restore_button").performScrollTo().performClick()
        compose.waitUntil(timeoutMillis = 10_000) {
            compose.onAllNodesWithText(string(R.string.nav_patient_home))
                .fetchSemanticsNodes().isNotEmpty()
        }
        node(R.string.nav_patient_home).assertIsDisplayed()
    }

    private class FakeProfileRepository : ProfileRepository {
        private val stored = mutableListOf<LocalUserProfile>()

        override suspend fun save(profile: LocalUserProfile) {
            stored.add(profile)
        }

        override suspend fun findById(profileId: String): LocalUserProfile? =
            stored.lastOrNull { it.profile.id == profileId }

        override suspend fun findLatestByRole(role: UserRole): LocalUserProfile? =
            stored.lastOrNull { it.profile.role == role }
    }
}
