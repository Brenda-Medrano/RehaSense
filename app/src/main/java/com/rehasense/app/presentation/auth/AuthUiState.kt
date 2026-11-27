package com.rehasense.app.presentation.auth

import com.rehasense.app.domain.auth.AuthenticatedUser

enum class AuthPage { SIGN_IN, REGISTER, RESET_PASSWORD }

data class AuthUiState(
    val page: AuthPage = AuthPage.SIGN_IN,
    val isSessionReady: Boolean = false,
    val currentUser: AuthenticatedUser? = null,
    val isBusy: Boolean = false,
    val error: String? = null,
    val notice: String? = null
)
