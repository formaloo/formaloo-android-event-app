package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.local.Sponsor

@Dao
abstract class SponsorDao : AppUIBaseDao<Sponsor>() {

    @Query("SELECT * FROM Sponsor ")
    abstract fun getSponsors(): PagingSource<Int, Sponsor>

    @Query("SELECT * FROM Sponsor ")
    abstract fun getSponsorList(): List<Sponsor>

    @Query("SELECT * FROM Sponsor WHERE sponsorSlug = :slug")
    abstract suspend fun getSponsor(slug: String): Sponsor

    @Query("DELETE FROM Sponsor WHERE sponsorSlug = :slug")
    abstract suspend fun deleteSponsor(slug: String)

    // ---
    @Query("DELETE FROM Sponsor")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(sponsors: Sponsor) {
        insert(sponsors)
    }

    suspend fun save(sponsors: List<Sponsor>) {
        insert(sponsors)
    }

}
