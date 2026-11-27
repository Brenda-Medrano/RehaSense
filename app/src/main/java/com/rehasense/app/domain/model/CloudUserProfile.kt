package com.rehasense.app.domain.model

/** Rol de presentación del documento users/{uid}, NO una autorización. */
enum class CloudUserRole {
    PATIENT,
    PHYSIOTHERAPIST
}

data class CloudUserProfile(
    val uid: String,
    val displayName: String,
    val role: CloudUserRole
)

/** El dato puede provenir de caché y contener escrituras aún no confirmadas. */
data class CloudUserSnapshot(
    val profile: CloudUserProfile,
    val fromCache: Boolean,
    val hasPendingWrites: Boolean
)
