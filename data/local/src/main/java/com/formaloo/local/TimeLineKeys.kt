package com.formaloo.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timelineKeys")
data class TimeLineKeys(
    @PrimaryKey
    val id: String,
    val prevkey: Int?,
    val nextKey: Int?
)
