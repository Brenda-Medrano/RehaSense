package com.rehasense.app.data.repository

import com.rehasense.app.data.local.entity.PatientEntity
import com.rehasense.app.data.local.entity.PhysiotherapistEntity
import com.rehasense.app.data.local.entity.ProfileEntity
import com.rehasense.app.data.local.entity.UserEntity
import com.rehasense.app.domain.model.LocalUserProfile
import com.rehasense.app.domain.model.PatientProfile
import com.rehasense.app.domain.model.PhysiotherapistProfile
import com.rehasense.app.domain.model.User
import com.rehasense.app.domain.model.UserProfile
import com.rehasense.app.domain.model.UserRole

/** Room entities are kept outside the domain model. */
internal data class ProfileRows(
    val user: UserEntity,
    val profile: ProfileEntity,
    val patient: PatientEntity? = null,
    val physiotherapist: PhysiotherapistEntity? = null
) {
    fun toDomain(): LocalUserProfile {
        require(profile.userId == user.id) { "Inconsistent user reference" }
        val role = UserRole.valueOf(profile.role)
        val typed: UserProfile = when (role) {
            UserRole.PATIENT -> {
                require(physiotherapist == null) { "Unexpected professional record" }
                val record = requireNotNull(patient) { "Missing patient record" }
                require(record.profileId == profile.id) { "Inconsistent patient reference" }
                PatientProfile(profile.id, user.id, record.id)
            }
            UserRole.PHYSIOTHERAPIST -> {
                require(patient == null) { "Unexpected patient record" }
                val record = requireNotNull(physiotherapist) { "Missing professional record" }
                require(record.profileId == profile.id) { "Inconsistent professional reference" }
                PhysiotherapistProfile(profile.id, user.id, record.id)
            }
        }
        return LocalUserProfile(User(user.id, user.name, user.createdAtEpochMillis), typed)
    }
}

internal fun LocalUserProfile.toProfileRows(): ProfileRows {
    val domainProfile = profile
    val userRow = UserEntity(user.id, user.name, user.createdAtEpochMillis)
    val profileRow = ProfileEntity(domainProfile.id, user.id, domainProfile.role.name)
    return when (domainProfile) {
        is PatientProfile -> ProfileRows(
            userRow, profileRow,
            patient = PatientEntity(domainProfile.patientId, domainProfile.id, user.createdAtEpochMillis)
        )
        is PhysiotherapistProfile -> ProfileRows(
            userRow, profileRow,
            physiotherapist = PhysiotherapistEntity(
                domainProfile.physiotherapistId, domainProfile.id, user.createdAtEpochMillis
            )
        )
    }
}
