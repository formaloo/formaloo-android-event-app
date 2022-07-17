package com.formaloo.model.form.stat

import java.io.Serializable

data class TrackData(
    var total: List<StatCount>? = null,
    var linkdin: List<StatCount>? = null,
    var facebook: List<StatCount>? = null
): Serializable
