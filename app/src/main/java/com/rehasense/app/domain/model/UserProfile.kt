package com.rehasense.app.domain.model

sealed interface UserProfile {
    val id: String
    val userId: String
    val role: UserRole
}

data class PatientProfile(
    override val id: String,
    override val userId: String,
    val patientId: String
) : UserProfile {
    init {
        validateProfileIdentity(id, userId)
        require(patientId.isNotBlank()) { "Patient ID must not be blank" }
    }

    override val role = UserRole.PATIENT
}

data class PhysiotherapistProfile(
    override val id: String,
    override val userId: String,
    val physiotherapistId: String
) : UserProfile {
    init {
        validateProfileIdentity(id, userId)
        require(physiotherapistId.isNotBlank()) { "Physiotherapist ID must not be blank" }
    }

    override val role = UserRole.PHYSIOTHERAPIST
}

private fun validateProfileIdentity(id: String, userId: String) {
    require(id.isNotBlank()) { "Profile ID must not be blank" }
    require(userId.isNotBlank()) { "User ID must not be blank" }
}
