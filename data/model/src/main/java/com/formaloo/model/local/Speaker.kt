package com.formaloo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity()
data class Speaker(
    @PrimaryKey
    var speakerSlug: String,
    var row: String?//tojson
) : Serializable
