package com.formaloo.model.boards.board.detail

import java.io.Serializable

data class BoardDetailRes(
    var status: Int? = null,
    var data: BoardDetailData? = null
) : Serializable {
    companion object {
        fun empty() = BoardDetailRes(0, null)

    }

    fun toBoardDetailRes() = BoardDetailRes(status, data)
}
