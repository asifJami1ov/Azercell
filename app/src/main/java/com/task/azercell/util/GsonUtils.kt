package com.task.azercell.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T : Any> T.json(): String = Gson().toJson(this, T::class.java)
inline fun <reified T : Any> String.fromJson(): T? {
    return if (this.isNotEmpty()) {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        gson.fromJson<T>(this, type)
    } else {
        null
    }
}