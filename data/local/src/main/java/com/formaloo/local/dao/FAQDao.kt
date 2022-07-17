package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.local.FAQ

@Dao
abstract class FAQDao : AppUIBaseDao<FAQ>() {

    @Query("SELECT * FROM FAQ ")
    abstract fun getFAQ(): PagingSource<Int, FAQ>

    @Query("SELECT * FROM FAQ ")
    abstract fun getFAQList(): List<FAQ>

    @Query("SELECT * FROM FAQ WHERE faqSlug = :slug")
    abstract suspend fun getFAQ(slug: String): FAQ

    @Query("DELETE FROM FAQ WHERE faqSlug = :slug")
    abstract suspend fun deleteFAQ(slug: String)

    // ---
    @Query("DELETE FROM FAQ")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(FAQs: FAQ) {
        insert(FAQs)
    }

    suspend fun save(FAQs: List<FAQ>) {
        insert(FAQs)
    }

}
