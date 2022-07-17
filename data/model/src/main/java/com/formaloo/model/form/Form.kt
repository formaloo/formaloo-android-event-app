package com.formaloo.model.form

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.formaloo.model.cat.Category
import com.formaloo.model.form.stat.Stats
import java.io.Serializable

@Entity
data class Form(
    @PrimaryKey
    var slug: String,
    var created_at: String? = null,
    var updated_at: String? = null,
    var updated_at_date: Long? = null,
    var form_type: String? = null,
    var shared_access: String? = null,
    var logo: String? = null,
    var logo_url: String? = null,
    var thumbnail: String? = null,
    var title: String? = null,
    var address: String? = null,
    var form_redirects_after_submit: String? = null,
    var copied_from: String? = null,
    var description: String? = null,
    var success_message: String? = null,
    var error_message: String? = null,
    var button_text: String? = null,
    var last_submit_time: String? = null,
    var primary_email: String? = null,
    var public_rows: Boolean? = null,
    var public_stats: Boolean? = null,
    var has_watermark: Boolean? = null,
    var spreadsheet_id: String? = null,
    var spreadsheet_url: String? = null,
    var live_dashboard_address: String? = null,
    var submit_count: Int = 0,
    var visit_count: Int = 0,
    var max_submit_count: String? = null,
    var submit_start_time: String? = null,
    var submit_end_time: String? = null,
    var needs_login: Boolean? = null,
    var send_emails_to: String? = null,
    var time_limit: String? = null,
    var submit_email_notif: Boolean? = null,
    var send_user_confirm: Boolean? = null,
    var submit_push_notif: Boolean? = null,
    var include_data_on_redirect: Boolean? = null,
    var text_color: String? = null,
    var field_color: String? = null,
    var button_color: String? = null,
    var border_color: String? = null,
    var background_color: String? = null,
    var background_image: String? = null,
    var submit_text_color: String? = null,
    var theme_color: String? = null,
    var theme_name: String? = null,
    var active: Boolean? = null,
    var show_answers: Boolean? = null,
    var show_title: Boolean? = null,
    var shuffle_fields: Boolean? = null,
    var shuffle_choices: Boolean? = null,
    var form_currency_type: String? = null,
    var show_calculations_score_result: Boolean? = null,
    @Embedded(prefix = "category")
    var category: Category? = null,
    var tags: ArrayList<Tag>? = null,
    var theme_config: ThemeConfig? = null,
    var fixed_payment_amount: String? = null,
    var fields_list: ArrayList<Fields>? = null,
    var stats: Stats? = null


) : Serializable
