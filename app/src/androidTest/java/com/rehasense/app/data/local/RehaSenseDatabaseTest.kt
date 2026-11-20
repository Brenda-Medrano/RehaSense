package com.rehasense.app.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rehasense.app.data.local.database.RehaSenseDatabase
import com.rehasense.app.data.local.entity.UserEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RehaSenseDatabaseTest {
    private lateinit var database: RehaSenseDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RehaSenseDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        if (::database.isInitialized) database.close()
    }

    @Test
    fun emptyDatabaseReturnsNull() {
        assertNull(database.userDao().findById("missing"))
    }

    @Test
    fun insertedUserCanBeRead() {
        val user = UserEntity("test-user", "Persona de prueba", 100L)
        database.userDao().insert(user)
        assertEquals(user, database.userDao().findById(user.id))
    }

    @Test
    fun duplicateIdIsRejectedWithoutReplacingUser() {
        val user = UserEntity("test-user", "Original", 100L)
        database.userDao().insert(user)
        try {
            database.userDao().insert(user.copy(name = "Duplicado"))
            fail("Expected a primary key constraint violation")
        } catch (_: SQLiteConstraintException) {
            assertEquals(user, database.userDao().findById(user.id))
        }
    }
}
