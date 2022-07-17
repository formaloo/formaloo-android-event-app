package com.formaloo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.formaloo.local.NewsKeys

@Dao
interface NewsKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveNewsKeys(redditKey: NewsKeys)

    @Insert(onConflict = REPLACE)
    suspend fun saveNewsKeys(keys: List<NewsKeys>)

    @Query("SELECT * FROM newsKeys ORDER BY id DESC")
    suspend fun getNewsKeys(): List<NewsKeys>

    @Query("SELECT * FROM newsKeys WHERE id = :id")
    suspend fun getNewsKeys(id: String): NewsKeys

    @Query("DELETE FROM newsKeys")
    abstract suspend fun deleteAllFromTable()

}
