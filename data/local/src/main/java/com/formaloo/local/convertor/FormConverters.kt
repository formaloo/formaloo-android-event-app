package com.formaloo.local.convertor

import androidx.room.TypeConverter
import com.formaloo.model.Row
import com.formaloo.model.boards.BlockLocation
import com.formaloo.model.boards.block.Block
import com.formaloo.model.boards.block.BlockItem
import com.formaloo.model.boards.block.statBlock.StatItems
import com.formaloo.model.boards.block.statBlock.StatSettings
import com.formaloo.model.cat.Category
import com.formaloo.model.form.*
import com.formaloo.model.form.stat.Stats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class FormConverters {
    @TypeConverter
    fun fromBlockLocation(data: BlockLocation?): String? {
        val type = object : TypeToken<BlockLocation>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toBlockLocation(json: String?): BlockLocation? {
        val type = object : TypeToken<BlockLocation>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromRow(data: Row?): String? {
        val type = object : TypeToken<Row>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toRow(json: String?): Row? {
        val type = object : TypeToken<Row>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromThemeConfig(data: ThemeConfig?): String? {
        val type = object : TypeToken<ThemeConfig>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toThemeConfig(json: String?): ThemeConfig? {
        val type = object : TypeToken<ThemeConfig>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromStats(data: Stats?): String? {
        val type = object : TypeToken<Stats>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toStats(json: String?): Stats? {
        val type = object : TypeToken<Stats>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromFormsOwner(data: FormsOwner?): String? {
        val type = object : TypeToken<FormsOwner>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFormsOwner(json: String?): FormsOwner? {
        val type = object : TypeToken<FormsOwner>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromWidgetSettings(data: WidgetSettings?): String? {
        val type = object : TypeToken<WidgetSettings>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toWidgetSettings(json: String?): WidgetSettings? {
        val type = object : TypeToken<WidgetSettings>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromDate(data: Date?): String? {
        val type = object : TypeToken<Date>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toDate(json: String?): Date? {
        val type = object : TypeToken<Date>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromForm(data: Form?): String? {
        val type = object : TypeToken<Form>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toForm(json: String?): Form? {
        val type = object : TypeToken<Form>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromCategory(data: Category?): String? {
        val type = object : TypeToken<Category>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toCategory(json: String?): Category? {
        val type = object : TypeToken<Category>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromStatSettings(data: StatSettings?): String? {
        val type = object : TypeToken<StatSettings>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toStatSettings(json: String?): StatSettings? {
        val type = object : TypeToken<StatSettings>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromFieldsList(data: ArrayList<Fields>?): String? {
        val type = object : TypeToken<ArrayList<Fields>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFieldsList(json: String?): ArrayList<Fields>? {
        val type = object : TypeToken<ArrayList<Fields>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromStatItemsList(data: ArrayList<StatItems>?): String? {
        val type = object : TypeToken<ArrayList<StatItems>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toStatItemsList(json: String?): ArrayList<StatItems>? {
        val type = object : TypeToken<ArrayList<StatItems>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromBlockItemList(data: ArrayList<BlockItem>?): String? {
        val type = object : TypeToken<ArrayList<BlockItem>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toBlockItemList(json: String?): ArrayList<BlockItem>? {
        val type = object : TypeToken<ArrayList<BlockItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromBlockList(data: ArrayList<Block>?): String? {
        val type = object : TypeToken<ArrayList<Block>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toBlockList(json: String?): ArrayList<Block>? {
        val type = object : TypeToken<ArrayList<Block>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromBlock(data: Block?): String? {
        val type = object : TypeToken<Block>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toBlock(json: String?): Block? {
        val type = object : TypeToken<Block>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromFields(data: Fields?): String? {
        val type = object : TypeToken<Fields>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFields(json: String?): Fields? {
        val type = object : TypeToken<Fields>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromTagList(data: ArrayList<Tag>?): String? {
        val type = object : TypeToken<ArrayList<Tag>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toTagList(json: String?): ArrayList<Tag>? {
        val type = object : TypeToken<ArrayList<Tag>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromCategoryList(data: ArrayList<Category>?): String? {
        val type = object : TypeToken<ArrayList<Category>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toCategoryList(json: String?): ArrayList<Category>? {
        val type = object : TypeToken<ArrayList<Category>>() {}.type
        return Gson().fromJson(json, type)
    }


    @TypeConverter
    fun fromFieldsSlugs(data: List<String>?): String? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toFieldsSlugs(json: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromMutiPart(data: HashMap<String, Fields>?): String? {
        val type = object : TypeToken<HashMap<String, Fields>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toMutiPart(json: String?): HashMap<String, Fields>? {
        val type = object : TypeToken<HashMap<String, Fields>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromReqHash(data: HashMap<String, String>?): String? {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toReqHash(json: String?): HashMap<String, String>? {
        val type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromCalculationItems(data: ArrayList<CalculationItem>?): String? {
        val type = object : TypeToken<ArrayList<CalculationItem>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toCalculationItems(json: String?): ArrayList<CalculationItem>? {
        val type = object : TypeToken<ArrayList<CalculationItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromChoiceItems(data: ArrayList<ChoiceItem>?): String? {
        val type = object : TypeToken<ArrayList<ChoiceItem>>() {}.type
        return Gson().toJson(data, type)
    }

    @TypeConverter
    fun toChoiceItems(json: String?): ArrayList<ChoiceItem>? {
        val type = object : TypeToken<ArrayList<ChoiceItem>>() {}.type
        return Gson().fromJson(json, type)
    }

}
