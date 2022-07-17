package com.formaloo.repository.board

import android.util.ArrayMap
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.formaloo.local.SpeakersKeys
import com.formaloo.local.dao.SpeakerDao
import com.formaloo.local.dao.SpeakerKeysDao
import com.formaloo.model.Converter
import com.formaloo.model.local.Speaker
import com.formaloo.remote.boards.BoardsDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
open class SpeakerListRemoteMediator(
    private val source: BoardsDatasource,
    private val speakerDao: SpeakerDao,
    private val speakerKeysDao: SpeakerKeysDao,
    private val boardSlug: String,
    private val blockSlug: String,
    private val force: Boolean,
    private val params: ArrayMap<String, Any>
) : RemoteMediator<Int, Speaker>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Speaker>
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
            val endOfPaginationReached = if ( force) {

                params["page"] = loadKey
                val response = source.getSharedBlockContentFlow(boardSlug, blockSlug, params)
                val body = response.body()
                val data = body?.data
//                Log.e(TAG, "load:SpeakerListRemoteMediator ${response.body()}")

                val speakerList = arrayListOf<Speaker>()
                data?.rows ?.forEach {
                    speakerList.add(Speaker(it.slug,Converter().from(it)))
                }
                Log.e(TAG, "speakerList:SpeakerListRemoteMediator ${speakerList.size}")

                val isEnd = data?.current_page == data?.page_count

                withContext(Dispatchers.IO) {
                    if (loadType == LoadType.REFRESH) {
                        Log.e(TAG, "loadType == LoadType.REFRESH: ")
                        speakerDao.deleteAllFromTable()
                        speakerKeysDao.deleteAllFromTable()

                    } else {

                    }


                    val prevOffset = if (loadKey == 1) null else loadKey - 1
                    val nextOffset = if (isEnd) null else loadKey + 1

                    val speakerKeys = speakerList.map {
                        SpeakersKeys(it.speakerSlug, prevOffset, nextOffset)
                    }

                    speakerKeysDao.saveSpeakersKeys(speakerKeys)
                    speakerDao.save(speakerList.toList())

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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Speaker>): SpeakersKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { speaker ->
                // Get the remote keys of the last item retrieved
                speakerKeysDao.getSpeakerKeys(speaker.speakerSlug)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Speaker>): SpeakersKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { speaker ->
                // Get the remote keys of the first items retrieved
                speakerKeysDao.getSpeakerKeys(speaker.speakerSlug)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Speaker>
    ): SpeakersKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.speakerSlug?.let {
                speakerKeysDao.getSpeakerKeys(it)
            }
        }
    }


}
