package com.example.laufen.training.presentation

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.laufen.training.utils.LocationProvider
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TrainingState(
    val duration: Long = 0,
    val distance: Long = 0,
    val burnedCalories: Int = 0,
    val route: List<LatLng> = listOf(),
)

class TrainingViewModel: ViewModel() {
    private val _trainingState = MutableStateFlow(TrainingState())
    val trainingState = _trainingState.asStateFlow()
    private val _latLng = MutableStateFlow( LatLng(1.35, 103.87))
    private val _route = MutableStateFlow<List<LatLng>>(listOf())
    val currentPosition = _latLng.asStateFlow()
    var isProviderInitialised = false
    private lateinit var locationProvider: LocationProvider
    private val currentLocationObserver = Observer<LatLng>() {
        updatePosition(it)
    }

    fun initLocation(context: Context) {
        locationProvider = LocationProvider(context)
        isProviderInitialised = true
        locationProvider.liveLocation.observeForever(currentLocationObserver)
        locationProvider.getUserLocation()
    }

    private fun updatePosition(latLng: LatLng) {
        _route.value += latLng
        _latLng.value = latLng
    }

    override fun onCleared() {
        locationProvider.liveLocation.removeObserver(currentLocationObserver)
        super.onCleared()
    }
}