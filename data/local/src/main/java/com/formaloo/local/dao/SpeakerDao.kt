package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.local.Speaker

@Dao
abstract class SpeakerDao : AppUIBaseDao<Speaker>() {

    @Query("SELECT * FROM Speaker ")
    abstract fun getSpeakers(): PagingSource<Int, Speaker>

    @Query("SELECT * FROM Speaker ")
    abstract fun getSpeakerList(): List<Speaker>

    @Query("SELECT * FROM Speaker WHERE speakerSlug = :slug")
    abstract suspend fun getSpeaker(slug: String): Speaker

    @Query("DELETE FROM Speaker WHERE speakerSlug = :slug")
    abstract suspend fun deleteSpeaker(slug: String)

    // ---
    @Query("DELETE FROM Speaker")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(speakers: Speaker) {
        insert(speakers)
    }

    suspend fun save(speakers: List<Speaker>) {
        insert(speakers)
    }

}
