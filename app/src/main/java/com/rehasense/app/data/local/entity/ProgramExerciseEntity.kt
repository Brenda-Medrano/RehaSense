package com.rehasense.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "program_exercises",
    primaryKeys = ["programId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = RehabilitationProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["exerciseId"])]
)
data class ProgramExerciseEntity(
    val programId: String,
    val exerciseId: String,
    val sortOrder: Int,
    val sets: Int,
    val repetitions: Int,
    val minAngleDegrees: Double?,
    val maxAngleDegrees: Double?
)
