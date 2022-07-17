package com.formaloo.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faqKeys")
data class FAQKeys(
    @PrimaryKey
    val id: String,
    val prevkey: Int?,
    val nextKey: Int?
)
