package com.example.laufen.training.presentation

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.laufen.training.service.Polyline
import com.example.laufen.training.service.Polylines
import com.example.laufen.training.service.TrainingService
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class TrainingViewModel: ViewModel() {
    val isServiceInited = false
    val _isPermissionGranted = MutableStateFlow(false)
    private var _isTracking = false
    private var _pathPoints = MutableStateFlow(mutableListOf<Polyline>())
    private var _curTimeInMillis = 0L

    val isPermissinoGranted = _isPermissionGranted.asStateFlow()

    fun grant() {
        _isPermissionGranted.value = true
    }


    private val trackingObserver = Observer<Boolean> { _isTracking = it }
    private val millisObserver = Observer<Long> { _curTimeInMillis = it }
    private val pathObserver = Observer<Polylines> { updatePathPoints(it) }

    private val _curAvgSpeed = MutableStateFlow(0)
    private val _curDistance = MutableStateFlow(0L)
    private val _curDuration = MutableStateFlow(0L)
    private val _curCalories = MutableStateFlow(0L)
    private val _curPosition = MutableStateFlow(LatLng(1.35, 103.87))

    val curAvgSpeed = _curAvgSpeed.asStateFlow()
    val curDistance = _curDistance.asStateFlow()
    val curDuration = _curDuration.asStateFlow()
    val curCalories = _curCalories.asStateFlow()
    val curPosition = _curPosition.asStateFlow()
    val polylines = _pathPoints.asStateFlow()

    private val _updateCounter = MutableStateFlow(0)
    val updateCounter = _updateCounter.asStateFlow()

    private fun setAvgSpeed(value: Int) {
        _curAvgSpeed.value = value
    }

    private fun setDuration(value: Long) {
        _curDuration.value = value
    }

    private fun setDistance(value: Long) {
        _curDistance.value = value
    }

    private fun setCalories(value: Long) {
        _curCalories.value = value
    }

    private fun updatePathPoints(polylines: Polylines) {
        _pathPoints.value = polylines
        Log.d("list", polylines.toString())
        _updateCounter.value += 1
        if (_pathPoints.value.isNotEmpty() && _pathPoints.value.last().isNotEmpty()) {
            _curPosition.value = _pathPoints.value.last().last()
        }
    }

    fun sendServiceCommand(action: String, context: Context) {
        Intent(context, TrainingService::class.java).also {
            it.action = action
            context.startService(it)
        }
    }

    init {
        TrainingService.isTracking.observeForever(trackingObserver)
//        TrainingService.curTimeInMillis.observeForever(millisObserver)
        TrainingService.pathPoints.observeForever(pathObserver)
    }

    override fun onCleared() {
        with(TrainingService) {
            isTracking.removeObserver(trackingObserver)
//            curTimeInMillis.removeObserver(millisObserver)
            pathPoints.removeObserver(pathObserver)
        }
        super.onCleared()
    }


//    private val _trainingState = MutableStateFlow(TrainingState())
//    val trainingState = _trainingState.asStateFlow()
//    private val _latLng = MutableStateFlow( LatLng(1.35, 103.87))
//    private val _route = MutableStateFlow<List<LatLng>>(listOf())
//    val currentPosition = _latLng.asStateFlow()
//    var isProviderInitialised = false
//    private lateinit var locationProvider: LocationProvider
//    private val currentLocationObserver = Observer<LatLng>() {
//        updatePosition(it)
//    }
//
//    fun initLocation(context: Context) {
//        locationProvider = LocationProvider(context)
//        isProviderInitialised = true
//        locationProvider.liveLocation.observeForever(currentLocationObserver)
//        locationProvider.getUserLocation()
//    }
//
//    private fun updatePosition(latLng: LatLng) {
//        _route.value += latLng
//        _latLng.value = latLng
//    }
//
}