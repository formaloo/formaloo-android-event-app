package com.formaloo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity()
data class TimeLine(
    @PrimaryKey
    var timeLineSlug: String,
    var row: String?//tojson
) : Serializable
