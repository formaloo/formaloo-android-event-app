package com.formaloo.repository.board

import android.util.ArrayMap
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.formaloo.common.exception.Failure
import com.formaloo.common.functional.Either
import com.formaloo.local.dao.*
import com.formaloo.model.Result
import com.formaloo.model.boards.block.Block
import com.formaloo.model.boards.block.detail.BlockDetailRes
import com.formaloo.model.boards.board.Board
import com.formaloo.model.boards.board.detail.BoardDetailRes
import com.formaloo.model.form.Form
import com.formaloo.model.local.*
import com.formaloo.model.Row
import com.formaloo.remote.boards.BoardsDatasource
import com.formaloo.repository.BaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

const val TAG = "BoardRepoImpl"

class BoardRepoImpl(
    private val source: BoardsDatasource,
    private val blockDao: BlockDao,
    private val boardDao: BoardDao,
    private val formDao: FormDao,
    private val speakerDao: SpeakerDao,
    private val timeLineDao: TimeLineDao,
    private val newsDao: NewsDao,
    private val speakerKeysDao: SpeakerKeysDao,
    private val newsKeysDao: NewsKeysDao,
    private val timeLineKeysDao: TimeLineKeysDao,
    private val sponsorDao: SponsorDao,
    private val sponsorKeysDao: SponsorKeysDao,
    private val faqDao: FAQDao,
    private val faqKeysDao: FAQKeysDao,
) : BoardRepo, BaseRepo() {
    override fun getSpeakerList(): List<Speaker> {
        return speakerDao.getSpeakerList()
    }

    override fun getSpobsorList(): List<Sponsor> {
        return sponsorDao.getSponsorList()
    }

    override suspend fun getNews(slug: String): News? {
        return newsDao.getNews(slug)

    }

    override fun getFAQList(): List<FAQ> {
        return faqDao.getFAQList()
    }

    override suspend fun getFAQ(slug: String): FAQ? {
        return faqDao.getFAQ(slug)

    }

    override suspend fun getSpeaker(slug: String): Speaker? {
        return speakerDao.getSpeaker(slug)

    }

    override suspend fun getSponsor(slug: String): Sponsor? {
        return sponsorDao.getSponsor(slug)

    }

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchSpeakers(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<Speaker>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = SpeakerListRemoteMediator(
                source,
                speakerDao,
                speakerKeysDao,
                boardSlug,
                blockSlug,
                force, params
            ),
            pagingSourceFactory = {
                speakerDao.getSpeakers()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchSponsors(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<Sponsor>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = SponsorListRemoteMediator(
                source,
                sponsorDao,
                sponsorKeysDao,
                boardSlug,
                blockSlug,
                force, params
            ),
            pagingSourceFactory = {
                sponsorDao.getSponsors()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchFAQs(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<FAQ>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = FAQListRemoteMediator(
                source,
                faqDao,
                faqKeysDao,
                boardSlug,
                blockSlug,
                force, params
            ),
            pagingSourceFactory = {
                faqDao.getFAQ()
            }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchTimeLines(
        boardSlug: String,
        blockSlug: String,
        force: Boolean, params: ArrayMap<String, Any>
    ): Flow<PagingData<TimeLine>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = TimeLineListRemoteMediator(
                source,
                timeLineDao,
                timeLineKeysDao,
                boardSlug,
                blockSlug,
                force, params
            ),
            pagingSourceFactory = {
                timeLineDao.getTimeLines()
            }
        ).flow
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun fetchGalley(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<Row>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GalleryPagingSource(source, boardSlug, blockSlug, params) }
        ).flow
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun fetchNews(
        boardSlug: String,
        blockSlug: String,
        force: Boolean,
        params: ArrayMap<String, Any>
    ): Flow<PagingData<News>> {
        return Pager(
            PagingConfig(pageSize = 40, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = NewsListRemoteMediator(
                source,
                newsDao,
                newsKeysDao,
                boardSlug,
                blockSlug,
                force, params
            ),
            pagingSourceFactory = {
                newsDao.getNews()
            }
        ).flow
    }

    override suspend fun getBlockFromDB(slug: String): Block? {
        return blockDao.getBlock(slug)

    }


    override suspend fun saveBoard(board: Board) {
        return boardDao.save(board)

    }

    override suspend fun getBoardData(address: String): Board? {
        return boardDao.getBoard(address)

    }


    override suspend fun saveForm(form: Form) {
        return formDao.save(form)

    }

    override suspend fun getFormData(slug: String): Form? {
        return formDao.getForm(slug)

    }


    override suspend fun saveBlockToDB(block: Block) {
        return blockDao.save(block)

    }

    override suspend fun getBlock(
        shared_board_slug: String,
        blockSlug: String
    ): Result<BlockDetailRes> {
        return withContext(Dispatchers.IO) {
            val call = source.getSharedBlockDetail(shared_board_slug, blockSlug)
            try {
                callRequest(call, { it.toBlockDetailRes() }, BlockDetailRes.empty())
            } catch (e: Exception) {
                Result.Error(IllegalStateException())
            }
        }
    }

    override suspend fun getSharedBoardDetail(
        shared_board_slug: String
    ): Either<Failure, BoardDetailRes> {
        val call = source.getSharedBoardDetail(shared_board_slug)
        return try {
            request(call, { it.toBoardDetailRes() }, BoardDetailRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }
    }
}



