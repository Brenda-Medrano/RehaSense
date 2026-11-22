package com.rehasense.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.rehasense.app.di.RehaSenseGraph
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.repository.ProfileRepository

/** The existing role destination owns the registration form and the resulting dashboard. */
@Composable
internal fun ProfileHomeScreen(
    role: UserRole,
    onChangeProfile: () -> Unit,
    repositoryOverride: ProfileRepository? = null
) {
    val appContext = LocalContext.current.applicationContext
    val repository = repositoryOverride ?: remember(appContext) { RehaSenseGraph.profileRepository(appContext) }
    val model: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory(repository))
    val state = model.uiState
    val profile = state.profile
    if (profile == null) {
        ProfileRegistrationScreen(
            role = role,
            state = state,
            onNameChanged = model::onNameChanged,
            onSubmit = { model.onSubmit(role) },
            onRestore = { model.onRestore(role) },
            onBack = onChangeProfile
        )
    } else {
        ProfileDashboard(profile = profile, onChangeProfile = onChangeProfile)
    }
}
