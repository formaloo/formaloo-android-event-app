package com.formaloo.model.boards.board.list

import java.io.Serializable

data class BoardListRes(
    var status: Int? = null,
    var data: BoardListData? = null
) : Serializable {
    companion object {
        fun empty() = BoardListRes(0, null)

    }

    fun toBoardListRes() = BoardListRes(status, data)
}
