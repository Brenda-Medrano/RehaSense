package com.rehasense.app.domain.repository

import com.rehasense.app.domain.model.LocalUserProfile

/** Persistence contract, implemented in the data layer. */
interface ProfileRepository {
    /** Persists the user and profile together, atomically, keyed by profile ID. */
    suspend fun save(profile: LocalUserProfile)

    /** Returns null when no profile with this ID exists. */
    suspend fun findById(profileId: String): LocalUserProfile?

    /** Recovery must be explicitly requested (there is no authentication yet). */
    suspend fun findLatestByRole(role: com.rehasense.app.domain.model.UserRole): LocalUserProfile?
}
