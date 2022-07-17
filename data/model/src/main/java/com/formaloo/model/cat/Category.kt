package com.formaloo.model.cat

import java.io.Serializable
import kotlin.collections.ArrayList

data class Category(
    var slug: String,
    var title: String? = null,
    var color: String? = null,
    var shared_access: String? = null,
    var subcategories: ArrayList<Category>? = null
) : Serializable
