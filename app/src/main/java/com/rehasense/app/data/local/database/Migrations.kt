package com.rehasense.app.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Explicit v1 -> v2 migration. Preserves `users` and adds the normalized tables.
 * Register with Room.databaseBuilder(...).addMigrations(MIGRATION_1_2) in patch 0009.
 * No destructive fallback is permitted for rehabilitation data.
 */
val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS `profiles` (`id` TEXT NOT NULL, `userId` TEXT NOT NULL, `role` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )""")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_profiles_userId` ON `profiles` (`userId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `patients` (`id` TEXT NOT NULL, `profileId` TEXT NOT NULL, `createdAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`profileId`) REFERENCES `profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )""")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_patients_profileId` ON `patients` (`profileId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `physiotherapists` (`id` TEXT NOT NULL, `profileId` TEXT NOT NULL, `createdAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`profileId`) REFERENCES `profiles`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )""")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_physiotherapists_profileId` ON `physiotherapists` (`profileId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `exercises` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `bodyRegion` TEXT NOT NULL, PRIMARY KEY(`id`))""")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `rehabilitation_programs` (`id` TEXT NOT NULL, `patientId` TEXT NOT NULL, `physiotherapistId` TEXT NOT NULL, `name` TEXT NOT NULL, `status` TEXT NOT NULL, `startAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`patientId`) REFERENCES `patients`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`physiotherapistId`) REFERENCES `physiotherapists`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rehabilitation_programs_patientId` ON `rehabilitation_programs` (`patientId`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_rehabilitation_programs_physiotherapistId` ON `rehabilitation_programs` (`physiotherapistId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `program_exercises` (`programId` TEXT NOT NULL, `exerciseId` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `sets` INTEGER NOT NULL, `repetitions` INTEGER NOT NULL, `minAngleDegrees` REAL, `maxAngleDegrees` REAL, PRIMARY KEY(`programId`, `exerciseId`), FOREIGN KEY(`programId`) REFERENCES `rehabilitation_programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_program_exercises_exerciseId` ON `program_exercises` (`exerciseId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `sessions` (`id` TEXT NOT NULL, `programId` TEXT NOT NULL, `startedAtEpochMillis` INTEGER NOT NULL, `finishedAtEpochMillis` INTEGER, `status` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`programId`) REFERENCES `rehabilitation_programs`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sessions_programId` ON `sessions` (`programId`)")

        db.execSQL("""CREATE TABLE IF NOT EXISTS `biomechanical_results` (`id` TEXT NOT NULL, `sessionId` TEXT NOT NULL, `exerciseId` TEXT NOT NULL, `joint` TEXT NOT NULL, `metric` TEXT NOT NULL, `value` REAL NOT NULL, `unit` TEXT NOT NULL, `source` TEXT NOT NULL, `recordedAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`sessionId`) REFERENCES `sessions`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT , FOREIGN KEY(`exerciseId`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_biomechanical_results_sessionId` ON `biomechanical_results` (`sessionId`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_biomechanical_results_exerciseId` ON `biomechanical_results` (`exerciseId`)")
    }
}
