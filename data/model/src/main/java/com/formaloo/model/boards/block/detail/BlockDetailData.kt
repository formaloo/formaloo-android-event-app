package com.formaloo.model.boards.block.detail

import com.formaloo.model.boards.block.Block
import java.io.Serializable

data class BlockDetailData(
    var block: Block? = null
) : Serializable
