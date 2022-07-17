package com.formaloo.model

import java.io.Serializable

data class TopFields(
    var title: String? = null,
    var slug: String? = null
): Serializable
