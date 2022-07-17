package com.formaloo.repository.board

import android.util.ArrayMap
import androidx.paging.PagingData
import com.formaloo.common.exception.Failure
import com.formaloo.common.functional.Either
import com.formaloo.model.Result
import com.formaloo.model.boards.block.Block
import com.formaloo.model.boards.block.detail.BlockDetailRes
import com.formaloo.model.boards.board.Board
import com.formaloo.model.boards.board.detail.BoardDetailRes
import com.formaloo.model.form.Form
import com.formaloo.model.local.*
import com.formaloo.model.Row
import kotlinx.coroutines.flow.Flow

interface BoardRepo {
    suspend fun getSharedBoardDetail(shared_board_slug: String): Either<Failure, BoardDetailRes>
    suspend fun getBlockFromDB(slug: String): Block?
    suspend fun saveBlockToDB(block: Block)
    suspend fun getBlock(shared_board_slug: String,blockSlug:String): Result<BlockDetailRes>
    fun getSpeakerList(): List<Speaker>
    suspend fun getSpeaker(slug: String): Speaker?
    fun fetchSpeakers(
        boardSlug: String,
        blockSlug: String,
        force: Boolean, params: ArrayMap<String, Any>
    ): Flow<PagingData<Speaker>>
    fun fetchTimeLines(
        boardSlug: String,
        blockSlug: String,
        force: Boolean, params: ArrayMap<String, Any>
    ): Flow<PagingData<TimeLine>>

    fun fetchNews(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<News>>

    suspend fun getNews(slug: String): News?

    fun fetchGalley(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<Row>>

    fun getSpobsorList(): List<Sponsor>
    suspend fun getSponsor(slug: String): Sponsor?

    fun fetchSponsors(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<Sponsor>>

    fun getFAQList(): List<FAQ>
    suspend fun getFAQ(slug: String): FAQ?

    fun fetchFAQs(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<FAQ>>

    suspend fun saveBoard(board: Board)
    suspend fun getBoardData(address: String): Board?
    suspend fun saveForm(form: Form)
    suspend fun getFormData(slug: String): Form?
}
