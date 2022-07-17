package com.formaloo.model.form.formDetail

import com.formaloo.model.form.Form
import java.io.Serializable

data class FormDetailData(
    var form: Form? = null,
) : Serializable
