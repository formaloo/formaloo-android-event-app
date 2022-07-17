package com.formaloo.model.boards.block.statBlock

import java.io.Serializable

data class StatItems(
    var slug: String? = null,
    var title: String? = null,
    var enabled: Boolean? = null,
    var data: Map<String,Any>? = null
) : Serializable
