package com.formaloo.model.form

import java.io.Serializable

data class Image(
    var image: String? = null,
    var slug: String? = null,
    var position: Int? = null

) : Serializable
