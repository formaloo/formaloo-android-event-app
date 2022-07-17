package com.formaloo.model.boards


enum class BlockType(val slug: String) {
    LINK("link"),
    BLOCK("block"),
    STATS("stats"),
    MENU("menu"),
    GROUP("group"),
    FORM_CHARTS("form_charts"),
    FORM_DISPLAY("form_display"),
    FORM_RESULT("form_result"),
    KANBAN("kanban"),
    REPORT("report")

}

enum class BlockSubType(val slug: String) {
    FIELDS_CHARTS("fields_charts"),
    ALL_CHARTS("all_charts"),
    DISPLAY_FIELDS("display_fields"),
    ALL_FIELDS("all_fields")

}

enum class FormDisplayType(val slug: String) {
    DISPLAY_MULTI_PAGE("display_multi_page"),
    DISPLAY_SINGLE_PAGE("display_single_page"),
    DISPLAY_OPEN_WEB("open_web"),

}
enum class StateItem(val slug: String) {
    TOTAL_VISITS("total-visits"),
    TOTAL_SUBMITS("total-submits"),
    DAILY_VISITS("daily-visits"),
    DAILY_SUBMITS("daily-submits"),

}

enum class ChartChoices(val slug: String) {
    BAR_CHART("bar_chart"),
    LINE_CHART("line_chart"),
    PIE_CHART("pie_chart"),
    GANTT_CHART("gantt_chart"),

}
