package com.formaloo.model.form

import java.io.Serializable

data class ThemeConfig(
    var widget_settings: WidgetSettings? = null,
    var background_shadow: Any? = null,
    var progress_percentage: Boolean? = null,
    var display_welcome_page: Boolean? = null,
    var background_type: String? = null
) : Serializable
