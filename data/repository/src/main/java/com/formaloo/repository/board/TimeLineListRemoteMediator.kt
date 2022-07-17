package com.formaloo.repository.board

import android.util.ArrayMap
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.formaloo.local.TimeLineKeys
import com.formaloo.local.dao.TimeLineDao
import com.formaloo.local.dao.TimeLineKeysDao
import com.formaloo.model.Converter
import com.formaloo.model.local.TimeLine
import com.formaloo.remote.boards.BoardsDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
open class TimeLineListRemoteMediator(
    private val source: BoardsDatasource,
    private val timeLineDao: TimeLineDao,
    private val timeLineKeysDao: TimeLineKeysDao,
    private val boardSlug: String,
    private val blockSlug: String,
    private val force: Boolean,
    private val params: ArrayMap<String, Any>
) : RemoteMediator<Int, TimeLine>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TimeLine>
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
                params["sort_by"] = "4x3g67Qu" //sort by date field

                val response = source.getSharedBlockContentFlow(boardSlug, blockSlug, params)
                val body = response.body()
                val data = body?.data
//                Log.e(TAG, "load:TimeLineListRemoteMediator ${response.body()}")

                val timeLineList = arrayListOf<TimeLine>()
                data?.rows?.forEach {
                    timeLineList.add(TimeLine(it.slug, Converter().from(it)))
                }
                Log.e(TAG, "timeLineList:TimeLineListRemoteMediator ${timeLineList.size}")

                val isEnd = data?.current_page == data?.page_count

                withContext(Dispatchers.IO) {
                    if (loadType == LoadType.REFRESH) {
                        Log.e(TAG, "loadType == LoadType.REFRESH: ")
                        timeLineDao.deleteAllFromTable()
                        timeLineKeysDao.deleteAllFromTable()

                    } else {

                    }


                    val prevOffset = if (loadKey == 1) null else loadKey - 1
                    val nextOffset = if (isEnd) null else loadKey + 1

                    val timeLineKeys = timeLineList.map {
                        TimeLineKeys(it.timeLineSlug, prevOffset, nextOffset)
                    }

                    timeLineKeysDao.saveTimeLineKeys(timeLineKeys)
                    timeLineDao.save(timeLineList.toList())

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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, TimeLine>): TimeLineKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { timeLine ->
                // Get the remote keys of the last item retrieved
                timeLineKeysDao.getTimeLineKeys(timeLine.timeLineSlug)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, TimeLine>): TimeLineKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { timeLine ->
                // Get the remote keys of the first items retrieved
                timeLineKeysDao.getTimeLineKeys(timeLine.timeLineSlug)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, TimeLine>
    ): TimeLineKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.timeLineSlug?.let {
                timeLineKeysDao.getTimeLineKeys(it)
            }
        }
    }


}
