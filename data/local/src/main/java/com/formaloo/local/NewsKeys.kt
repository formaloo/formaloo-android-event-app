package com.formaloo.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsKeys")
data class NewsKeys(
    @PrimaryKey
    val id: String,
    val prevkey: Int?,
    val nextKey: Int?
)
