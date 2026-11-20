package com.rehasense.app.presentation.profile

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rehasense.app.domain.model.UserRole

/** The existing role destination owns the registration form and the resulting dashboard. */
@Composable
internal fun ProfileHomeScreen(role: UserRole, onChangeProfile: () -> Unit) {
    val model: ProfileViewModel = viewModel()
    val state = model.uiState
    val profile = state.profile
    if (profile == null) {
        ProfileRegistrationScreen(
            role = role,
            state = state,
            onNameChanged = model::onNameChanged,
            onSubmit = { model.onSubmit(role) },
            onBack = onChangeProfile
        )
    } else {
        ProfileDashboard(profile = profile, onChangeProfile = onChangeProfile)
    }
}
