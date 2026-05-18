package com.forza.teams.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamsDao {

    @Query("SELECT * FROM teams")
    fun observeAll(): Flow<List<TeamRoomEntity>>

    @Transaction
    suspend fun replaceAll(items: List<TeamRoomEntity>) {
        deleteAll()
        insertAll(items)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TeamRoomEntity>)

    @Query("DELETE FROM teams")
    suspend fun deleteAll()
}
