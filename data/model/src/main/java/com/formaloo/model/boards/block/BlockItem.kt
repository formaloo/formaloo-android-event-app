package com.formaloo.model.boards.block

import java.io.Serializable

data class BlockItem(
    var sub_items: ArrayList<BlockItem>? = null,
    var block: Block? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var slug: String,
    var title: String? = null,
    var type: String? = null,
    var subtype: String? = null,
    var position: Int? = null,
    var link: String? = null,
    var description: String? = null,
    var color: String? = null
) : Serializable
