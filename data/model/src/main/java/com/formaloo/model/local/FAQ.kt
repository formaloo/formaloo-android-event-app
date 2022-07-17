package com.formaloo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity()
data class FAQ(
    @PrimaryKey
    var faqSlug: String,
    var  row: String?
): Serializable
