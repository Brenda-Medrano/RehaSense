package com.rehasense.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rehasense.app.data.local.database.MIGRATION_1_2
import com.rehasense.app.data.local.database.RehaSenseDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMigration1To2Test {
    @Test fun migrationPreservesVersionOneUsers() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = "rehasense_migration_test.db"
        context.deleteDatabase(name)
        try {
            // Reproduce the actual v1 users table without requiring a fabricated schema JSON.
            context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null).use { oldDb ->
                oldDb.execSQL(
                    "CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, " +
                        "`name` TEXT NOT NULL, `createdAtEpochMillis` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                )
                oldDb.execSQL(
                    "INSERT INTO users (`id`, `name`, `createdAtEpochMillis`) VALUES ('old-user', 'Legacy test', 5)"
                )
                oldDb.version = 1
            }
            Room.databaseBuilder(context, RehaSenseDatabase::class.java, name)
                .addMigrations(MIGRATION_1_2)
                .build().use { migrated ->
                    assertEquals("Legacy test", migrated.userDao().findById("old-user")?.name)
                    assertNotNull(migrated.rehabilitationDao())
                }
        } finally {
            context.deleteDatabase(name)
        }
    }
}
