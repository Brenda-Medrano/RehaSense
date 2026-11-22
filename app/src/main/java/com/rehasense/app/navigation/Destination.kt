package com.rehasense.app.navigation

import com.rehasense.app.domain.model.UserRole

internal enum class Destination(val route: String) {
    WELCOME("welcome"),
    PROFILE_SELECTION("profile_selection"),
    PATIENT_HOME("patient_home"),
    PHYSIOTHERAPIST_HOME("physiotherapist_home");

    companion object {
        fun homeFor(role: UserRole): Destination = when (role) {
            UserRole.PATIENT -> PATIENT_HOME
            UserRole.PHYSIOTHERAPIST -> PHYSIOTHERAPIST_HOME
        }
    }
}
