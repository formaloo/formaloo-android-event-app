package com.formaloo.model.form

import java.io.Serializable

data class CreateFormReq(
    var title: String? = null,
    var description: String? = null,
    var active: Boolean? = false,
    var send_user_confirm: Boolean? = false
) : Serializable
