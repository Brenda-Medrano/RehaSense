package com.rehasense.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rehasense.app.data.local.dao.RehabilitationDao
import com.rehasense.app.data.local.dao.UserDao
import com.rehasense.app.data.local.entity.*

/** Version 2 adds the normalized rehabilitation schema; no persistent UI data is opened yet. */
@Database(
    entities = [
        UserEntity::class,
        ProfileEntity::class,
        PatientEntity::class,
        PhysiotherapistEntity::class,
        ExerciseEntity::class,
        RehabilitationProgramEntity::class,
        ProgramExerciseEntity::class,
        SessionEntity::class,
        BiomechanicalResultEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class RehaSenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun rehabilitationDao(): RehabilitationDao
}
