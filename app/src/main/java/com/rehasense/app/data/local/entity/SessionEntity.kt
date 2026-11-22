package com.rehasense.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sessions",
    foreignKeys = [ForeignKey(
        entity = RehabilitationProgramEntity::class,
        parentColumns = ["id"],
        childColumns = ["programId"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [Index(value = ["programId"])]
)
data class SessionEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val startedAtEpochMillis: Long,
    val finishedAtEpochMillis: Long?,
    val status: String
)
