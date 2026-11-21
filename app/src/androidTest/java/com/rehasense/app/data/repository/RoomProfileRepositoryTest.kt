package com.rehasense.app.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rehasense.app.data.local.database.RehaSenseDatabase
import com.rehasense.app.domain.model.PatientProfile
import com.rehasense.app.domain.model.PhysiotherapistProfile
import com.rehasense.app.domain.model.UserRole
import com.rehasense.app.domain.usecase.CreateUserProfile
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** All names and database records in these tests are fictitious. */
@RunWith(AndroidJUnit4::class)
class RoomProfileRepositoryTest {
    private lateinit var database: RehaSenseDatabase
    private lateinit var repository: RoomProfileRepository

    @Before fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RehaSenseDatabase::class.java).build()
        repository = RoomProfileRepository(database)
    }

    @After fun tearDown() {
        if (::database.isInitialized) database.close()
    }

    private fun profile(name: String, role: UserRole, prefix: String) =
        CreateUserProfile(
            newId = sequenceOf("$prefix-user", "$prefix-profile", "$prefix-role").iterator()::next,
            now = { 100L }
        )(name, role)

    @Test fun patientIsSavedAndReadWithAllThreeIdentifiers() = runBlocking {
        val original = profile("Ana", UserRole.PATIENT, "p")
        repository.save(original)
        assertEquals(original, repository.findById("p-profile"))
        assertEquals("p-role", (repository.findById("p-profile")!!.profile as PatientProfile).patientId)
    }

    @Test fun physiotherapistIsSavedAndReadWithAllThreeIdentifiers() = runBlocking {
        val original = profile("Luis", UserRole.PHYSIOTHERAPIST, "t")
        repository.save(original)
        assertEquals(original, repository.findById("t-profile"))
        assertEquals("t-role", (repository.findById("t-profile")!!.profile as PhysiotherapistProfile).physiotherapistId)
    }

    @Test fun missingProfileReturnsNull() = runBlocking {
        assertNull(repository.findById("not-found"))
        assertNull(repository.findLatestByRole(UserRole.PATIENT))
    }

    @Test fun latestProfileIsFilteredByRole() = runBlocking {
        val first = profile("Paciente", UserRole.PATIENT, "p")
        val second = profile("Profesional", UserRole.PHYSIOTHERAPIST, "t")
        repository.save(first)
        repository.save(second)
        assertEquals(first, repository.findLatestByRole(UserRole.PATIENT))
        assertEquals(second, repository.findLatestByRole(UserRole.PHYSIOTHERAPIST))
    }

    @Test fun failedInsertRollsBackUserAndProfileAsOneTransaction() = runBlocking {
        val first = profile("Primero", UserRole.PATIENT, "first")
        repository.save(first)
        // Different user but conflicting profile primary key. The first INSERT must roll back.
        val conflicting = profile("Segundo", UserRole.PATIENT, "second").let {
            it.copy(profile = (it.profile as PatientProfile).copy(id = first.profile.id))
        }
        assertTrue(runCatching { repository.save(conflicting) }.isFailure)
        assertNull(database.userDao().findById("second-user"))
        assertEquals(first, repository.findById("first-profile"))
    }
}
