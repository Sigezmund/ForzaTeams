package com.forza.teams.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TeamRoomEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamsDao(): TeamsDao
}
