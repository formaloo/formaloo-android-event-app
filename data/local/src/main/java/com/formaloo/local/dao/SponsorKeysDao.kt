package com.formaloo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.formaloo.local.SponsorsKeys

@Dao
interface SponsorKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveSponsorsKeys(redditKey: SponsorsKeys)

    @Insert(onConflict = REPLACE)
    suspend fun saveSponsorsKeys(keys: List<SponsorsKeys>)

    @Query("SELECT * FROM sponsorsKeys ORDER BY id DESC")
    suspend fun getSponsorsKeys(): List<SponsorsKeys>

    @Query("SELECT * FROM sponsorsKeys WHERE id = :id")
    suspend fun getSponsorKeys(id: String): SponsorsKeys

    @Query("DELETE FROM sponsorsKeys")
    abstract suspend fun deleteAllFromTable()

}
