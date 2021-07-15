package com.rwq.plugins.utils

import com.google.gson.Gson

class GsonUtils {
    companion object {
        private val gson = Gson()
        fun <T> parseData(data: String,clazz: Class<T>): T {
            return gson.fromJson(data, clazz)
        }

    }

}
