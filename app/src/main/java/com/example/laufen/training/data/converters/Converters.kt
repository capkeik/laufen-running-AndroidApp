package com.example.laufen.training.data.converters

import android.location.Location
import androidx.room.TypeConverter
import com.example.laufen.training.service.Polylines
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String) : Polylines{
        val listType = object: TypeToken<Polylines>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: Polylines): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}