package com.formaloo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.formaloo.local.FAQKeys

@Dao
interface FAQKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveFAQKeys(redditKey: FAQKeys)

    @Insert(onConflict = REPLACE)
    suspend fun saveFAQKeys(keys: List<FAQKeys>)

    @Query("SELECT * FROM faqKeys ORDER BY id DESC")
    suspend fun getFAQKeys(): List<FAQKeys>

    @Query("SELECT * FROM faqKeys WHERE id = :id")
    suspend fun getFAQKeys(id: String): FAQKeys

    @Query("DELETE FROM faqKeys")
    abstract suspend fun deleteAllFromTable()

}
