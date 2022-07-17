package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.local.News

@Dao
abstract class NewsDao : AppUIBaseDao<News>() {

    @Query("SELECT * FROM News ")
    abstract fun getNews(): PagingSource<Int, News>

    @Query("SELECT * FROM News ")
    abstract fun getNewsList(): List<News>

    @Query("SELECT * FROM News WHERE newsSlug = :slug")
    abstract suspend fun getNews(slug: String): News

    @Query("DELETE FROM News WHERE newsSlug = :slug")
    abstract suspend fun deleteNews(slug: String)

    // ---
    @Query("DELETE FROM News")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(Newss: News) {
        insert(Newss)
    }

    suspend fun save(Newss: List<News>) {
        insert(Newss)
    }

}
