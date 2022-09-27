package com.example.laufen.training.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import javax.inject.Singleton


class LocationProvider(context: Context) {

    private val client
            by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val locations = mutableListOf<LatLng>()
    val liveLocation = MutableLiveData<LatLng>()

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        client.lastLocation.addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            liveLocation.value = latLng
        }
    }
}