package com.formaloo.model.boards.block

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.formaloo.model.boards.block.statBlock.StatItems
import com.formaloo.model.form.Fields
import com.formaloo.model.form.Form
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey
    var slug: String,
    var created_at: String? = null,
    var updated_at: String? = null,
    var type: String? = null,
    var subtype: String? = null,
    var display_type: String? = null,
    var title: String? = null,
    var location: String? = null,
    var color: String? = null,
    var position: Int? = null,
    /**
     * When getting the stats block, aside from the base data, we will get the `stats` and `stats_settings` objects, which contain the stats and their setting.

    The `stats` objects contain a list of the available stats, inside their `data` field. If one of the stats is disabled, the `data` is sent as `null`.

    The `stats_setitngs` is used for updating the `stats`'s data, used in **Update Stats Block.**
     */
    var stats: ArrayList<StatItems>? = null,
    /**
     * Form Display Block
     */
    @SerializedName("form")
    var form: Form? = null,

    /**
     * Form Result Block
     */
//    var rows: ArrayList<Row>? = null,
    var items: ArrayList<Block>? = null,

    var sub_items: ArrayList<Block>? = null,
    var block: Block? = null,
    var link: String? = null,
    var description: String? = null,

    var columns_field: Fields? = null,
    var items_field: Fields? = null,
    var featured_image_field: Fields? = null,

//    var report: TableReport? = null,


) : Serializable
