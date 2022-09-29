package com.example.laufen.training.presentation.training

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laufen.training.Const
import com.example.laufen.training.domain.entity.Training
import com.example.laufen.training.domain.repository.TrainingRepository
import com.example.laufen.training.service.Polyline
import com.example.laufen.training.service.Polylines
import com.example.laufen.training.service.TrainingService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class TrainingInfoState(
    val avgSpeed: Int = 0,
    val distance: Long = 0L,
    val duration: Long = 0L,
    val calories: Long = 0L,
)

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
): ViewModel() {
    val isLocated = false
    private var _isTracking = MutableStateFlow(false)
    private var _pathPoints = MutableStateFlow(mutableListOf<Polyline>())

    private val trackingObserver = Observer<Boolean> { _isTracking.value = it }
    private val millisObserver = Observer<Long> { setDuration(it) }
    private val pathObserver = Observer<Polylines> { updatePathPoints(it) }
    private val distanceObserver = Observer<Long> {
        setDistance(it)
        setAvgSpeed(
            with (_trainingInfoState.value) {
                if (duration != 0L) {
                    (distance / duration).toInt()
                } else 0
            }
        )
    }

    private val _trainingInfoState = MutableStateFlow(TrainingInfoState())
    private val _curPosition = MutableStateFlow(LatLng(1.35, 103.87))

    val trainingInfoState = _trainingInfoState.asStateFlow()
    val curPosition = _curPosition.asStateFlow()
    val polylines = _pathPoints.asStateFlow()
    val isTracking = _isTracking

    private fun setAvgSpeed(value: Int) {
        _trainingInfoState.update {
            it.copy(avgSpeed = value)
        }
    }

    private fun setDuration(value: Long) {
        _trainingInfoState.update {
            it.copy(duration = value)
        }
    }

    private fun setDistance(value: Long) {
        _trainingInfoState.update {
            it.copy(distance = value)
        }
    }

    private fun setCalories(value: Long) {
        _trainingInfoState.update {
            it.copy(calories = value)
        }
    }

    private fun updatePathPoints(polylines: Polylines) {
        _pathPoints.value = polylines
        Log.d("list", polylines.toString())
        if (_pathPoints.value.isNotEmpty() && _pathPoints.value.last().isNotEmpty()) {
            _curPosition.value = _pathPoints.value.last().last()
        }
    }

    private fun sendServiceCommand(action: String, context: Context) {
        Intent(context, TrainingService::class.java).also {
            it.action = action
            context.startService(it)
        }
    }

    fun startTracking(context: Context) {
        sendServiceCommand(
            action = Const.ACTION_START_OR_RESUME,
            context
        )
    }

    fun pauseTracking(context: Context) {
        sendServiceCommand(
            action = Const.ACTION_PAUSE,
            context
        )
    }

    fun stopTracking(context: Context) {
        sendServiceCommand(
            action = Const.ACTION_STOP,
            context
        )
    }


    init {
        TrainingService.isTracking.observeForever(trackingObserver)
        TrainingService.timeInMillis.observeForever(millisObserver)
        TrainingService.pathPoints.observeForever(pathObserver)
        TrainingService.distance.observeForever(distanceObserver)
    }

    override fun onCleared() {
        with(TrainingService) {
            isTracking.removeObserver(trackingObserver)
            timeInMillis.removeObserver(millisObserver)
            pathPoints.removeObserver(pathObserver)
            distance.removeObserver(distanceObserver)
        }
        super.onCleared()
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    fun initLocation(context: Context) {
        val fusedLocationProviderClient = FusedLocationProviderClient(context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            _curPosition.value = LatLng(location.latitude, location.longitude)
        }
    }

    fun saveTrainingToDB() {
        val training = Training(
            timestamp = Calendar.getInstance().timeInMillis,
            duration = _trainingInfoState.value.duration,
            distance = _trainingInfoState.value.distance,
            burnedCalories = _trainingInfoState.value.calories.toInt(),
            route = polylines.value
        )
        viewModelScope.launch {
            trainingRepository.addTraining(training)
        }
    }
}