package com.formaloo.model.boards.board.detail

import com.formaloo.model.boards.board.Board
import java.io.Serializable

data class BoardDetailData(
    var board: Board? = null
) : Serializable
