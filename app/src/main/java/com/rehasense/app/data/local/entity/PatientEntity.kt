package com.rehasense.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "patients",
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["id"],
        childColumns = ["profileId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["profileId"], unique = true)]
)
data class PatientEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val createdAtEpochMillis: Long
)
