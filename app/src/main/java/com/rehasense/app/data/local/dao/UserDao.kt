package com.rehasense.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rehasense.app.data.local.entity.UserEntity

/** Blocking operations: call from a background thread; never from the UI thread. */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun findById(id: String): UserEntity?
}
