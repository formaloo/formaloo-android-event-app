package com.formaloo.model.boards.block.list

import java.io.Serializable

data class BlockListRes(
    var status: Int? = null,
    var data: BlockListData? = null
) : Serializable {
    companion object {
        fun empty() = BlockListRes(0, null)

    }

    fun toBlockListRes() = BlockListRes(status, data)
}
