package com.rehasense.app.di

import android.content.Context
import androidx.room.Room
import com.rehasense.app.data.local.database.MIGRATION_1_2
import com.rehasense.app.data.local.database.RehaSenseDatabase
import com.rehasense.app.data.repository.RoomProfileRepository
import com.rehasense.app.domain.repository.ProfileRepository

/** Minimal application-scoped dependency container: no Hilt/Gradle plugin required. */
object RehaSenseGraph {
    @Volatile private var storedRepository: ProfileRepository? = null

    fun profileRepository(context: Context): ProfileRepository =
        storedRepository ?: synchronized(this) {
            storedRepository ?: Room.databaseBuilder(
                context.applicationContext,
                RehaSenseDatabase::class.java,
                "rehasense.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
                .let(::RoomProfileRepository)
                .also { storedRepository = it }
        }
}
