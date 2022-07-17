package com.formaloo.model.form.stat

import java.io.Serializable

data class StatCount(
    var date: String? = null,
    var count: Int =0
): Serializable
