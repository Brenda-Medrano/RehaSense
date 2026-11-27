package com.rehasense.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehasense.app.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/** Controla Authentication; no consulta ni concede permisos clínicos. */
class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = mutableState.asStateFlow()

    private fun updateState(change: (AuthUiState) -> AuthUiState) {
        mutableState.value = change(mutableState.value)
    }

    init {
        viewModelScope.launch {
            try {
                repository.session.collect { user ->
                    updateState { it.copy(isSessionReady = true, currentUser = user) }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                updateState {
                    it.copy(isSessionReady = true, error = authErrorMessage(failure))
                }
            }
        }
    }

    fun openPage(page: AuthPage) {
        if (mutableState.value.isBusy || mutableState.value.currentUser != null) return
        updateState { it.copy(page = page, error = null, notice = null) }
    }

    fun signIn(email: String, password: String) {
        if (mutableState.value.isBusy) return
        val validation = AuthFormValidator.signIn(email, password)
        if (validation != null) {
            updateState { it.copy(error = validation, notice = null) }
            return
        }
        viewModelScope.launch {
            updateState { it.copy(isBusy = true, error = null, notice = null) }
            try {
                val user = repository.signIn(email.trim(), password)
                updateState { it.copy(currentUser = user, isSessionReady = true, isBusy = false) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                updateState {
                    it.copy(isBusy = false, error = authErrorMessage(failure))
                }
            }
        }
    }

    fun register(email: String, password: String, confirmation: String) {
        if (mutableState.value.isBusy) return
        val validation = AuthFormValidator.register(email, password, confirmation)
        if (validation != null) {
            updateState { it.copy(error = validation, notice = null) }
            return
        }
        viewModelScope.launch {
            updateState { it.copy(isBusy = true, error = null, notice = null) }
            try {
                val user = repository.register(email.trim(), password)
                updateState {
                    it.copy(
                        currentUser = user,
                        isSessionReady = true,
                        isBusy = false,
                        notice = "Cuenta creada. El perfil clínico todavía no está configurado."
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                updateState { it.copy(isBusy = false, error = authErrorMessage(failure)) }
            }
        }
    }

    fun resetPassword(email: String) {
        if (mutableState.value.isBusy) return
        val validation = AuthFormValidator.email(email)
        if (validation != null) {
            updateState { it.copy(error = validation, notice = null) }
            return
        }
        viewModelScope.launch {
            updateState { it.copy(isBusy = true, error = null, notice = null) }
            try {
                repository.sendPasswordReset(email.trim())
                updateState {
                    it.copy(
                        isBusy = false,
                        notice = "Si ese correo está registrado, recibirás instrucciones para restablecer tu contraseña."
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (failure: Exception) {
                updateState { it.copy(isBusy = false, error = authErrorMessage(failure)) }
            }
        }
    }

    fun signOut() {
        if (mutableState.value.isBusy) return
        try {
            repository.signOut()
            updateState {
                it.copy(
                    currentUser = null,
                    isSessionReady = true,
                    isBusy = false,
                    page = AuthPage.SIGN_IN,
                    error = null,
                    notice = "Sesión cerrada."
                )
            }
        } catch (failure: Exception) {
            updateState { it.copy(error = authErrorMessage(failure)) }
        }
    }
}
