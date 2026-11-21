package com.rehasense.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rehasense.app.data.local.entity.*

/** Blocking test-stage DAO. In app code, invoke off the main thread (patch 0009). */
@Dao
interface RehabilitationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertProfile(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPatient(patient: PatientEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPhysiotherapist(physiotherapist: PhysiotherapistEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertExercise(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertProgram(program: RehabilitationProgramEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertProgramExercise(exercise: ProgramExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertSession(session: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertResult(result: BiomechanicalResultEntity)

    @Query("SELECT * FROM program_exercises WHERE programId = :programId ORDER BY sortOrder ASC")
    fun findExercisesForProgram(programId: String): List<ProgramExerciseEntity>

    @Query("SELECT * FROM biomechanical_results WHERE sessionId = :sessionId ORDER BY recordedAtEpochMillis ASC")
    fun findResultsForSession(sessionId: String): List<BiomechanicalResultEntity>
}
