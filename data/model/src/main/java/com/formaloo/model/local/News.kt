package com.formaloo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity()
data class News(
    @PrimaryKey
    var newsSlug: String,
    var  row: String?
): Serializable
