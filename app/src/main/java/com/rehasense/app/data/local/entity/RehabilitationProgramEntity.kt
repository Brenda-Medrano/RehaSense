package com.rehasense.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rehabilitation_programs",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PhysiotherapistEntity::class,
            parentColumns = ["id"],
            childColumns = ["physiotherapistId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["patientId"]), Index(value = ["physiotherapistId"])]
)
data class RehabilitationProgramEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val physiotherapistId: String,
    val name: String,
    val status: String,
    val startAtEpochMillis: Long
)
