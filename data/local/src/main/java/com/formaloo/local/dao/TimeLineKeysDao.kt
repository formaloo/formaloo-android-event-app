package com.formaloo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.formaloo.local.TimeLineKeys

@Dao
interface TimeLineKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveTimeLineKeys(redditKey: TimeLineKeys)

    @Insert(onConflict = REPLACE)
    suspend fun saveTimeLineKeys(keys: List<TimeLineKeys>)

    @Query("SELECT * FROM timeLineKeys ORDER BY id DESC")
    suspend fun getTimeLineKeys(): List<TimeLineKeys>

    @Query("SELECT * FROM timeLineKeys WHERE id = :id")
    suspend fun getTimeLineKeys(id: String): TimeLineKeys

    @Query("DELETE FROM TimeLineKeys")
    abstract suspend fun deleteAllFromTable()

}
