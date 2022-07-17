package com.formaloo.model.boards

import com.formaloo.model.boards.block.Block
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BlockLocation(
    var header: ArrayList<Block>? = null,
    var footer: ArrayList<Block>? = null,
    var main: ArrayList<Block>? = null,
    @SerializedName("left-sidebar")
    var leftSideBar: ArrayList<Block>? = null,
    @SerializedName("right-sidebar")
    var rightSideBar: ArrayList<Block>? = null
) : Serializable
