package com.formaloo.model.boards.block.statBlock

import java.io.Serializable

data class StatSettings(
    var slug: String? = null,
    var title: String? = null,
    var enabled: Boolean? = null
) : Serializable
