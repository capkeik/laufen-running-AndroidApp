package com.example.laufen.training.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.laufen.R
import com.example.laufen.training.Const.ACTION_PAUSE
import com.example.laufen.training.Const.ACTION_START_OR_RESUME
import com.example.laufen.training.Const.ACTION_STOP
import com.example.laufen.training.Const.FASTEST_LOCATION_INTERVAL
import com.example.laufen.training.Const.LOCATION_UPDATE_INTERVAL
import com.example.laufen.training.Const.NOTIFICATION_CHANNEL_ID
import com.example.laufen.training.Const.NOTIFICATION_CHANNEL_NAME
import com.example.laufen.training.Const.NOTIFICATION_ID
import com.example.laufen.training.Const.TIMER_UPDATE_INTERVAL
import com.example.laufen.training.utils.Common
import com.example.laufen.training.utils.Formatter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "service"
typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrainingService : LifecycleService() {

    private var isFirstRun = true
    var isKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    private lateinit var curNotificationBuilder: NotificationCompat.Builder

    private val timeInSeconds = MutableLiveData<Long>()

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val timeInMillis = MutableLiveData<Long>()
        val distance = MutableLiveData<Long>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        distance.postValue(0L)
        timeInMillis.postValue(0L)
        timeInSeconds.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
            updateNotificationState(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startTimer()
                    }
                }
                ACTION_PAUSE -> {
                    pauseService()
                }
                ACTION_STOP -> {
                    killService()
                }
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (Common.checkIfPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d(TAG, "NEW LOCATION: ${location.latitude}, ${location.longitude}")
                    }
                }
            }
        }
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                // time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                // post the new lapTime
                timeInMillis.postValue(timeRun + lapTime)
                if (timeInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeInSeconds.postValue(timeInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
            updateDistance()
        }
    }

    private fun updateDistance() {
        if (pathPoints.value?.isNotEmpty() == true) {
            if (pathPoints.value!!.last().size > 1) {
                val list = pathPoints.value!!.last()
                val size = pathPoints.value!!.last().size
                val dist = SphericalUtil.computeDistanceBetween(
                    list[size - 1], list[size - 2]
                )
                distance.postValue(dist.toLong())
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeInSeconds.observe(this) {
            if (!isKilled) {
                val notification = curNotificationBuilder
                    .setContentText(
                        Formatter.formatTime(
                            it * 1000L
                        ) + "\n" + Formatter.formatDistance(
                            distance.value ?: 0
                        ) + " km"
                    )
                    .build()
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }
    }

    private fun updateNotificationState(isTracking: Boolean) {
        val actionText = if (isTracking) "Pause" else "Resume"
        val actionIcon = if (isTracking) {
            R.drawable.ic_round_pause_circle_outline_24
        } else {
            R.drawable.ic_round_play_circle_outline_24
        }
        val pendingIntent = if (isTracking) {
            val intent = Intent(this, TrainingService::class.java).apply {
                action = ACTION_PAUSE
            }
            getService(
                this,
                1,
                intent,
                FLAG_UPDATE_CURRENT + FLAG_IMMUTABLE
            )
        } else {
            val intent = Intent(this, TrainingService::class.java).apply {
                action = ACTION_START_OR_RESUME
            }
            getService(
                this,
                2,
                intent,
                FLAG_UPDATE_CURRENT + FLAG_IMMUTABLE
            )
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        if (!isKilled) {
            curNotificationBuilder = curNotificationBuilder
                .addAction(actionIcon, actionText, pendingIntent)
            notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
    fun killService() {
        isKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }
}
