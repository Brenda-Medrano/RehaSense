package com.rehasense.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "biomechanical_results",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["sessionId"]), Index(value = ["exerciseId"])]
)
data class BiomechanicalResultEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val joint: String,
    val metric: String,
    val value: Double,
    val unit: String,
    val source: String,
    val recordedAtEpochMillis: Long
)
