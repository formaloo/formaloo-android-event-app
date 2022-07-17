package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.local.TimeLine

@Dao
abstract class TimeLineDao : AppUIBaseDao<TimeLine>() {

    @Query("SELECT * FROM TimeLine ")
    abstract fun getTimeLines(): PagingSource<Int, TimeLine>

    @Query("SELECT * FROM TimeLine ")
    abstract fun getTimeLineList(): List<TimeLine>

    @Query("SELECT * FROM TimeLine WHERE timeLineSlug = :slug")
    abstract suspend fun getTimeLine(slug: String): TimeLine

    @Query("DELETE FROM TimeLine WHERE timeLineSlug = :slug")
    abstract suspend fun deleteTimeLine(slug: String)

    // ---
    @Query("DELETE FROM TimeLine")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(timeLines: TimeLine) {
        insert(timeLines)
    }

    suspend fun save(timeLines: List<TimeLine>) {
        insert(timeLines)
    }

}
