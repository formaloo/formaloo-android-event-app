package com.formaloo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.formaloo.local.SpeakersKeys

@Dao
interface SpeakerKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveSpeakersKeys(redditKey: SpeakersKeys)

    @Insert(onConflict = REPLACE)
    suspend fun saveSpeakersKeys(keys: List<SpeakersKeys>)

    @Query("SELECT * FROM speakersKeys ORDER BY id DESC")
    suspend fun getSpeakersKeys(): List<SpeakersKeys>

    @Query("SELECT * FROM speakersKeys WHERE id = :id")
    suspend fun getSpeakerKeys(id: String): SpeakersKeys

    @Query("DELETE FROM SpeakersKeys")
    abstract suspend fun deleteAllFromTable()

}
