package com.formaloo.model.boards.block.list

import com.formaloo.model.boards.block.Block
import java.io.Serializable

data class BlockListData(
    var blocks: List<Block>? = null,
    var previous: String? = null,
    var next: String? = null,
    var count: Int? = null,
    var current_page: Int? = null,
    var page_count: Int? = null,
    var page_size: Int? = null
): Serializable
