package com.formaloo.model.boards.board.list

import com.formaloo.model.boards.board.Board
import java.io.Serializable

data class BoardListData(
    var boards: List<Board>? = null,
    var previous: String? = null,
    var next: String? = null,
    var count: Int? = null,
    var current_page: Int? = null,
    var page_count: Int? = null,
    var page_size: Int? = null
): Serializable
