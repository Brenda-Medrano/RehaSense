package com.rehasense.app.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/** Saving is asynchronous; the dashboard is displayed only after the transaction succeeds. */
internal class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val form = ProfileForm()

    var uiState by mutableStateOf(ProfileUiState())
        private set

    fun onNameChanged(name: String) {
        if (uiState.isBusy) return
        uiState = form.editName(uiState, name).copy(saveError = false, restoreNotFound = false)
    }

    fun onSubmit(role: UserRole) {
        if (uiState.isBusy || uiState.profile != null) return
        val result = form.submit(uiState, role)
        val candidate = result.profile
        if (candidate == null) {
            uiState = result
            return
        }
        uiState = result.copy(profile = null, isBusy = true, saveError = false, restoreNotFound = false)
        viewModelScope.launch {
            try {
                repository.save(candidate)
                uiState = result.copy(isBusy = false, saveError = false)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                uiState = result.copy(profile = null, isBusy = false, saveError = true)
            }
        }
    }

    fun onRestore(role: UserRole) {
        if (uiState.isBusy || uiState.profile != null) return
        uiState = uiState.copy(isBusy = true, saveError = false, restoreNotFound = false)
        viewModelScope.launch {
            try {
                val saved = repository.findLatestByRole(role)
                uiState = uiState.copy(
                    isBusy = false,
                    profile = saved,
                    name = saved?.user?.name.orEmpty(),
                    restoreNotFound = saved == null
                )
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                uiState = uiState.copy(isBusy = false, saveError = true)
            }
        }
    }

    class Factory(private val repository: ProfileRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                "Unknown ViewModel class"
            }
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
    }
}
