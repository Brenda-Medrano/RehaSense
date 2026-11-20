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
    override val role = UserRole.PATIENT
}

data class PhysiotherapistProfile(
    override val id: String,
    override val userId: String,
    val physiotherapistId: String
) : UserProfile {
    override val role = UserRole.PHYSIOTHERAPIST
}
