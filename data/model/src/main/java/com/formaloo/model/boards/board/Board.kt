package com.formaloo.model.boards.board

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.formaloo.model.boards.BlockLocation
import com.formaloo.model.form.Form
import java.io.Serializable

@Entity
data class Board(
    @PrimaryKey
    var slug: String,
    var primary_form: Form? = null,
    var blocks: BlockLocation? = null,
    var is_primary: Boolean? = null,
    var title: String? = null,
    var share_address: String? = null,
    var description: String? = null,
) : Serializable
