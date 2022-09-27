package com.example.laufen.training.data.converters

import android.location.Location
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String) : List<Location>{
        val listType = object: TypeToken<List<Location>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<Location>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}