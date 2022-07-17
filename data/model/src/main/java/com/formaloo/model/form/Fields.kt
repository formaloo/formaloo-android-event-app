package com.formaloo.model.form

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Fields(
    var slug: String? = null,
    var answer_description: String? = null,
    var created_at: String? = null,
    var type: String? = null,
    var sub_type: String? = null,
    var title: String? = null,
    var description: String? = null,
    var thumbnail_type: String? = null,
    var position: Int? = null,
    var required: Boolean? = null,
    @SerializedName("is_random_sortable")
    var random_sortable: Boolean? = null,
    var admin_only: Boolean? = null,
    var alias: String? = null,
    var unique: Boolean? = null,
    var json_key: Any? = null,
    var max_length: String? = null,
    var max_size: String? = null,
    var min_value: String? = null,
    var max_value: String? = null,
    var from_date: String? = null,
    var to_date: String? = null,
    var from_time: String? = null,
    var to_time: String? = null,
    var file_type: String? = null,
    var currency: String? = null,
    var rating_type: String? = null,
    var phone_type: String? = null,
    var video_link: String? = null,
    var is_calculatable: Boolean? = null,
    var shuffle_choices: Boolean? = null,
    var has_other_choice: Boolean? = null,
    var unit_price: String? = null,
    var images: ArrayList<Image>? = null,
    var calculation_items: ArrayList<CalculationItem>? = null,
    var choice_items: ArrayList<ChoiceItem>? =  null,
    var choice_groups: ArrayList<ChoiceItem>? =  null,
    var column_groups: ArrayList<ChoiceItem>? =  null,
    var raw_stats: Map<String,Any>? =  null,
    var readable_stats: Map<String,Any>? =  null,
    var logo: String? = null,



    ):Serializable
