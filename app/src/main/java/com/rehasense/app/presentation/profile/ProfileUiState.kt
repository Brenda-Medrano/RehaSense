package com.rehasense.app.presentation.profile

import com.rehasense.app.domain.model.LocalUserProfile

internal data class ProfileUiState(
    val name: String = "",
    val showNameError: Boolean = false,
    val profile: LocalUserProfile? = null,
    val isBusy: Boolean = false,
    val saveError: Boolean = false,
    val restoreNotFound: Boolean = false
)
