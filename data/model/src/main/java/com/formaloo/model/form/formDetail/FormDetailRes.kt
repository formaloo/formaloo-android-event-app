package com.formaloo.model.form.formDetail

import java.io.Serializable

data class FormDetailRes(
    var status: Int? = null,
    var data: FormDetailData? = null
) : Serializable {
    companion object {
        fun empty() = FormDetailRes(0, null)

    }

    fun toFormDetailRes() = FormDetailRes(status, data)
}
