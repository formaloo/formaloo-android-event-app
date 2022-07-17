package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.boards.board.Board

@Dao
abstract class BoardDao : AppUIBaseDao<Board>() {

    @Query("SELECT * FROM Board ")
    abstract fun getBoards(): PagingSource<Int, Board>

    @Query("SELECT * FROM Board ")
    abstract fun getBoardList(): List<Board>

    @Query("SELECT * FROM Board WHERE share_address = :slug")
    abstract suspend fun getBoard(slug: String): Board

    @Query("DELETE FROM Board WHERE slug = :slug")
    abstract suspend fun deleteBoard(slug: String)

    // ---
    @Query("DELETE FROM Board")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(boards: Board) {
        insert(boards)
    }

    suspend fun save(boards: List<Board>) {
        insert(boards)
    }

}
