package com.rehasense.app.data.repository

import com.rehasense.app.data.local.database.RehaSenseDatabase
import com.rehasense.app.data.local.entity.ProfileEntity
import com.rehasense.app.domain.model.LocalUserProfile
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Transactions group all rows of a local profile. No database work runs on the UI thread. */
class RoomProfileRepository(
    private val database: RehaSenseDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProfileRepository {
    override suspend fun save(profile: LocalUserProfile) {
        withContext(ioDispatcher) {
            val rows = profile.toProfileRows()
            database.runInTransaction {
                database.userDao().insert(rows.user)
                database.rehabilitationDao().insertProfile(rows.profile)
                rows.patient?.let { database.rehabilitationDao().insertPatient(it) }
                rows.physiotherapist?.let { database.rehabilitationDao().insertPhysiotherapist(it) }
            }
        }
    }

    override suspend fun findById(profileId: String): LocalUserProfile? = withContext(ioDispatcher) {
        val row = database.rehabilitationDao().findProfileById(profileId) ?: return@withContext null
        load(row)
    }

    override suspend fun findLatestByRole(role: UserRole): LocalUserProfile? = withContext(ioDispatcher) {
        val row = database.rehabilitationDao().findLatestProfileByRole(role.name) ?: return@withContext null
        load(row)
    }

    private fun load(row: ProfileEntity): LocalUserProfile {
        val user = requireNotNull(database.userDao().findById(row.userId)) { "Missing user record" }
        return ProfileRows(
            user = user,
            profile = row,
            patient = database.rehabilitationDao().findPatientByProfileId(row.id),
            physiotherapist = database.rehabilitationDao().findPhysiotherapistByProfileId(row.id)
        ).toDomain()
    }
}
