package com.example.laufen.training.data.converters

import android.location.Location
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String) : List<LatLng>{
        val listType = object: TypeToken<List<LatLng>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<LatLng>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}