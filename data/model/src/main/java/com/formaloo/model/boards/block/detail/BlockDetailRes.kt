package com.formaloo.model.boards.block.detail

import java.io.Serializable

data class BlockDetailRes(
    var status: Int? = null,
    var data: BlockDetailData? = null
) : Serializable {
    companion object {
        fun empty() = BlockDetailRes(0, null)

    }

    fun toBlockDetailRes() = BlockDetailRes(status, data)
}
