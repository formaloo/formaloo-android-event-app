package com.formaloo.model.form.stat

import com.formaloo.model.form.Fields
import java.io.Serializable

data class Stats(
    var total: Int? = null,
    var fields: ArrayList<Fields>? = null,
    var track_data: TrackData?  = null,
    var submits: List<StatCount>?  = null
): Serializable
