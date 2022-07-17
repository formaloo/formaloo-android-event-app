package com.formaloo.repository.board

import android.util.ArrayMap
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.formaloo.local.NewsKeys
import com.formaloo.local.dao.NewsDao
import com.formaloo.local.dao.NewsKeysDao
import com.formaloo.model.Converter
import com.formaloo.model.local.News
import com.formaloo.remote.boards.BoardsDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
open class NewsListRemoteMediator(
    private val source: BoardsDatasource,
    private val newsDao: NewsDao,
    private val newsKeysDao: NewsKeysDao,
    private val boardSlug: String,
    private val blockSlug: String,
    private val force: Boolean,
    private val params: ArrayMap<String, Any>
) : RemoteMediator<Int, News>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {
        Log.e(TAG, "load:force $force")
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(-1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    // If remoteKeys is null, that means the refresh result is not in the database yet.
                    // We can return Success with `endOfPaginationReached = false` because Paging
                    // will call this method again if RemoteKeys becomes non-null.
                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                    // the end of pagination for prepend.
                    val prevOffset = remoteKeys?.prevkey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )

                    prevOffset
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)

                    val nextOffset = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )

                    nextOffset

                }
            }
            val endOfPaginationReached = if (force) {

                params["page"] = loadKey
                val response = source.getSharedBlockContentFlow(boardSlug, blockSlug, params)
                val body = response.body()
                val data = body?.data
//                Log.e(TAG, "load:NewsListRemoteMediator ${response.body()}")

                val newsList = arrayListOf<News>()
                data?.rows?.forEach {
                    newsList.add(News(it.slug, Converter().from(it)))
                }
                Log.e(TAG, "newsList:NewsListRemoteMediator ${newsList.size}")

                val isEnd = data?.current_page == data?.page_count

                withContext(Dispatchers.IO) {
                    if (loadType == LoadType.REFRESH) {
                        Log.e(TAG, "loadType == LoadType.REFRESH: ")
                        newsDao.deleteAllFromTable()
                        newsKeysDao.deleteAllFromTable()

                    } else {

                    }


                    val prevOffset = if (loadKey == 1) null else loadKey - 1
                    val nextOffset = if (isEnd) null else loadKey + 1

                    val newsKeys = newsList.map {
                        NewsKeys(it.newsSlug, prevOffset, nextOffset)
                    }

                    newsKeysDao.saveNewsKeys(newsKeys)
                    newsDao.save(newsList.toList())

                }

                isEnd

            } else {
                true
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, News>): NewsKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { news ->
                // Get the remote keys of the last item retrieved
                newsKeysDao.getNewsKeys(news.newsSlug)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, News>): NewsKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { news ->
                // Get the remote keys of the first items retrieved
                newsKeysDao.getNewsKeys(news.newsSlug)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, News>
    ): NewsKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.newsSlug?.let {
                newsKeysDao.getNewsKeys(it)
            }
        }
    }


}
