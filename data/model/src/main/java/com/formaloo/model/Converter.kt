package com.formaloo.model


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
     fun<T> from(data: T?): String? {
        val type = object : TypeToken<T>() {}.type
        return Gson().toJson(data, type)
    }

    inline  fun<reified T> to(json: String?): T? {
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type)
    }

}
