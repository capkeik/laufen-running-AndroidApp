package com.example.laufen.training.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

private const val TAG = "Common"

class Common {
    companion object {
        fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
            return (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED)
        }

        fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {

            val activity = context as Activity

            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission
            )
        }
    }
}