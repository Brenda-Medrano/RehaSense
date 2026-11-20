package com.rehasense.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rehasense.app.data.local.dao.UserDao
import com.rehasense.app.data.local.entity.UserEntity

/** Initial schema; not opened by the application until repository integration. */
@Database(entities = [UserEntity::class], version = 1, exportSchema = true)
abstract class RehaSenseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
