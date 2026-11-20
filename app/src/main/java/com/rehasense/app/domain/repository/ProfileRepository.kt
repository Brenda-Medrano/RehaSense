package com.rehasense.app.domain.repository

import com.rehasense.app.domain.model.LocalUserProfile

/** Local persistence contract. A data-layer implementation will follow. */
interface ProfileRepository {
    /** Persists the user and profile together, atomically, keyed by profile ID. */
    suspend fun save(profile: LocalUserProfile)

    /** Returns null when no profile with this ID exists. */
    suspend fun findById(profileId: String): LocalUserProfile?
}
