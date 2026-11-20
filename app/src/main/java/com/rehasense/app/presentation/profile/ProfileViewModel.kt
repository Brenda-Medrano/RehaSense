package com.rehasense.app.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rehasense.app.domain.model.UserRole

/** One instance per navigation entry; intentionally does not persist personal data. */
internal class ProfileViewModel : ViewModel() {
    private val form = ProfileForm()

    var uiState by mutableStateOf(ProfileUiState())
        private set

    fun onNameChanged(name: String) {
        uiState = form.editName(uiState, name)
    }

    fun onSubmit(role: UserRole) {
        uiState = form.submit(uiState, role)
    }
}
