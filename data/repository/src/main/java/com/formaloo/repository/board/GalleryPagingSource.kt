package com.formaloo.repository.board

import android.util.ArrayMap
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.formaloo.model.Row
import com.formaloo.remote.boards.BoardsDatasource
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1
private const val NETWORK_PAGE_SIZE = 10

class GalleryPagingSource(
    private val source: BoardsDatasource,
    private val boardSlug: String,
    private val blockSlug: String,
    private val rowParams: ArrayMap<String, Any>
) : PagingSource<Int, Row>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Row> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        rowParams["page"]=position
        
        return try {
            val response = source.getSharedBlockContentFlow(boardSlug, blockSlug, rowParams)
            val data = response.body()?.data
            Timber.e("GalleryPagingSource ${response.raw()}")

            val rows = data?.rows?: arrayListOf()
            val nextKey = if (data?.next==null) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position +1
            }
            LoadResult.Page(
                data = rows,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Row>): Int? {
        Timber.e("getRefreshKey")
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
