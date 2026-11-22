package com.rehasense.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.repository.ProfileRepository
import com.rehasense.app.presentation.profile.ProfileSelectionScreen
import com.rehasense.app.presentation.profile.ProfileHomeScreen
import com.rehasense.app.presentation.welcome.WelcomeScreen

@Composable
fun RehaSenseNavHost(
    modifier: Modifier = Modifier,
    profileRepository: ProfileRepository? = null
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.WELCOME.route,
        modifier = modifier
    ) {
        composable(Destination.WELCOME.route) {
            WelcomeScreen(onStart = {
                navController.navigate(Destination.PROFILE_SELECTION.route) {
                    launchSingleTop = true
                }
            })
        }
        composable(Destination.PROFILE_SELECTION.route) {
            ProfileSelectionScreen(
                onBack = { navController.popBackStack() },
                onContinue = { role ->
                    navController.navigate(Destination.homeFor(role).route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Destination.PATIENT_HOME.route) {
            ProfileHomeScreen(
                role = UserRole.PATIENT,
                repositoryOverride = profileRepository,
                onChangeProfile = { navController.popBackStack() }
            )
        }
        composable(Destination.PHYSIOTHERAPIST_HOME.route) {
            ProfileHomeScreen(
                role = UserRole.PHYSIOTHERAPIST,
                repositoryOverride = profileRepository,
                onChangeProfile = { navController.popBackStack() }
            )
        }
    }
}
