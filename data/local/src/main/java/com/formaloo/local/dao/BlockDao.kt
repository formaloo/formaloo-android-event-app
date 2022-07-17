package com.formaloo.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.formaloo.model.boards.block.Block

@Dao
abstract class BlockDao : AppUIBaseDao<Block>() {

    @Query("SELECT * FROM Block ")
    abstract fun getBlocks(): PagingSource<Int, Block>

    @Query("SELECT * FROM Block ")
    abstract fun getBlockList(): List<Block>

    @Query("SELECT * FROM Block WHERE type='stats' OR type='menu' ")
    abstract fun getLeftSideBlockList(): List<Block>

    @Query("SELECT * FROM Block WHERE type!='stats' AND type!='menu' ")
    abstract fun getMainBlockList(): List<Block>

    @Query("SELECT * FROM Block WHERE slug = :slug")
    abstract suspend fun getBlock(slug: String): Block

    @Query("DELETE FROM Block WHERE slug = :slug")
    abstract suspend fun deleteBlock(slug: String)

    // ---
    @Query("DELETE FROM Block")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(blocks: Block) {
        insert(blocks)
    }

    suspend fun save(blocks: List<Block>) {
        insert(blocks)
    }

}
