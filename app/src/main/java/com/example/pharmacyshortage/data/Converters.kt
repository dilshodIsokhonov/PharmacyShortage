package com.example.pharmacyshortage.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * This class has helper functions to convert List into String
 * and vise versa
 */
@ProvidedTypeConverter
class Converters {
    @TypeConverter fun stringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter fun listToString(list: List<String>):String {
        return Gson().toJson(list)
    }
}