package com.formaloo.remote.boards

import com.formaloo.common.Constants.VERSION3Dot3
import com.formaloo.model.boards.block.content.BlockContentRes
import com.formaloo.model.boards.block.detail.BlockDetailRes
import com.formaloo.model.boards.board.detail.BoardDetailRes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface BoardsService {

    companion object {

        private const val SHARED_BOARD = "${VERSION3Dot3}shared-boards/{board_share_address}"
        private const val SHARED_BOARDS_BLOCK =
            "${VERSION3Dot3}shared-boards/{board_share_address}/blocks/{block_slug}"
        private const val SHARED_BOARDS_BLOCK_CONTENT =
            "${VERSION3Dot3}shared-boards/{board_share_address}/blocks/{block_slug}/content/"

    }

    @GET(SHARED_BOARDS_BLOCK)
    fun getSharedBlockDetail(
        @Path("board_share_address") form_slug: String,
        @Path("block_slug") block_slug: String,
    ): Call<BlockDetailRes>


    @GET(SHARED_BOARDS_BLOCK_CONTENT)
    @JvmSuppressWildcards
    suspend fun getSharedBlockContentFlow(
        @Path("board_share_address") form_slug: String,
        @Path("block_slug") block_slug: String,
        @QueryMap field_Choice: Map<String, Any>?
    ): Response<BlockContentRes>


    @GET(SHARED_BOARD)
    fun getSharedBoardDetail(
        @Path("board_share_address") board_slug: String
    ): Call<BoardDetailRes>


}
