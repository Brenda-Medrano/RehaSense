package com.rehasense.app.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rehasense.app.data.local.database.RehaSenseDatabase
import com.rehasense.app.data.local.entity.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** Database-level integrity tests. All data is fictitious and kept in memory. */
@RunWith(AndroidJUnit4::class)
class RehabilitationDatabaseTest {
    private lateinit var database: RehaSenseDatabase

    @Before fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RehaSenseDatabase::class.java).build()
    }

    @After fun tearDown() {
        if (::database.isInitialized) database.close()
    }

    private fun seedParticipants() {
        database.userDao().insert(UserEntity("user-p", "Paciente de prueba", 1L))
        database.userDao().insert(UserEntity("user-t", "Profesional de prueba", 1L))
        database.rehabilitationDao().insertProfile(ProfileEntity("profile-p", "user-p", "PATIENT"))
        database.rehabilitationDao().insertProfile(ProfileEntity("profile-t", "user-t", "PHYSIOTHERAPIST"))
        database.rehabilitationDao().insertPatient(PatientEntity("patient", "profile-p", 1L))
        database.rehabilitationDao().insertPhysiotherapist(PhysiotherapistEntity("therapist", "profile-t", 1L))
    }

    private fun seedProgramAndExercise() {
        seedParticipants()
        database.rehabilitationDao().insertExercise(ExerciseEntity("exercise", "Flexion de rodilla", "Prueba", "KNEE"))
        database.rehabilitationDao().insertProgram(
            RehabilitationProgramEntity("program", "patient", "therapist", "Programa de prueba", "DRAFT", 10L)
        )
    }

    @Test fun profileCannotReferenceMissingUser() {
        assertConstraintViolation {
            database.rehabilitationDao().insertProfile(ProfileEntity("profile", "missing", "PATIENT"))
        }
    }

    @Test fun userCannotHaveDuplicateProfiles() {
        database.userDao().insert(UserEntity("user", "Usuario ficticio", 1L))
        database.rehabilitationDao().insertProfile(ProfileEntity("profile-1", "user", "PATIENT"))
        assertConstraintViolation {
            database.rehabilitationDao().insertProfile(ProfileEntity("profile-2", "user", "PATIENT"))
        }
    }

    @Test fun patientRequiresExistingProfile() {
        assertConstraintViolation {
            database.rehabilitationDao().insertPatient(PatientEntity("patient", "missing", 1L))
        }
    }

    @Test fun professionalRequiresExistingProfile() {
        assertConstraintViolation {
            database.rehabilitationDao().insertPhysiotherapist(
                PhysiotherapistEntity("professional", "missing", 1L)
            )
        }
    }

    @Test fun programRequiresRealPatientAndProfessional() {
        assertConstraintViolation {
            database.rehabilitationDao().insertProgram(
                RehabilitationProgramEntity("program", "missing-p", "missing-t", "Draft", "DRAFT", 1L)
            )
        }
    }

    @Test fun programExercisesAreOrderedAndLinked() {
        seedProgramAndExercise()
        database.rehabilitationDao().insertProgramExercise(
            ProgramExerciseEntity("program", "exercise", 0, 3, 10, 30.0, 90.0)
        )
        assertEquals(1, database.rehabilitationDao().findExercisesForProgram("program").size)
        assertEquals("exercise", database.rehabilitationDao().findExercisesForProgram("program")[0].exerciseId)
        assertConstraintViolation {
            database.rehabilitationDao().insertProgramExercise(
                ProgramExerciseEntity("program", "missing", 1, 3, 10, null, null)
            )
        }
    }

    @Test fun sessionRequiresExistingProgram() {
        assertConstraintViolation {
            database.rehabilitationDao().insertSession(
                SessionEntity("session", "missing", 100L, null, "PLANNED")
            )
        }
    }

    @Test fun biomechanicalResultRequiresSessionAndExercise() {
        seedProgramAndExercise()
        assertConstraintViolation {
            database.rehabilitationDao().insertResult(
                BiomechanicalResultEntity("result", "missing", "exercise", "knee", "angle", 70.0, "degree", "CAMERA", 101L)
            )
        }
    }

    @Test fun sessionResultIsQueryableWithoutNetwork() {
        seedProgramAndExercise()
        database.rehabilitationDao().insertProgramExercise(
            ProgramExerciseEntity("program", "exercise", 0, 2, 8, null, null)
        )
        database.rehabilitationDao().insertSession(
            SessionEntity("session", "program", 100L, null, "COMPLETED")
        )
        val result = BiomechanicalResultEntity(
            "result", "session", "exercise", "knee", "flexion_angle", 70.0, "degree", "MANUAL_TEST", 101L
        )
        database.rehabilitationDao().insertResult(result)
        assertEquals(listOf(result), database.rehabilitationDao().findResultsForSession("session"))
    }

    private fun assertConstraintViolation(block: () -> Unit) {
        try {
            block()
            fail("Expected SQLite foreign-key or uniqueness constraint violation")
        } catch (_: SQLiteConstraintException) {
            // Constraint violation is the expected outcome.
        }
    }
}
