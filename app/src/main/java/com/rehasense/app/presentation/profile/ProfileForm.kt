package com.rehasense.app.presentation.profile

import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.usecase.CreateUserProfile

/** Registration transitions; independent of Android and reusable in JVM tests. */
internal class ProfileForm(private val createUserProfile: CreateUserProfile = CreateUserProfile()) {
    fun editName(state: ProfileUiState, name: String): ProfileUiState {
        if (state.profile != null) return state
        return state.copy(name = name, showNameError = false)
    }

    fun submit(state: ProfileUiState, role: UserRole): ProfileUiState {
        if (state.profile != null) return state
        if (state.name.isBlank()) return state.copy(showNameError = true)
        val profile = createUserProfile(state.name, role)
        return state.copy(name = profile.user.name, showNameError = false, profile = profile)
    }
}
