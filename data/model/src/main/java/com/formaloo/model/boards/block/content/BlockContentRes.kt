package com.formaloo.model.boards.block.content

import java.io.Serializable

data class BlockContentRes(
    var status: Int? = null,
    var data: BlockContentData? = null
) : Serializable {
    companion object {
        fun empty() = BlockContentRes(0, null)

    }

    fun toBlockContentRes() = BlockContentRes(status, data)
}
