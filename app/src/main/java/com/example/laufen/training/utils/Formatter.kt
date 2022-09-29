package com.example.laufen.training.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class Formatter {
    companion object {
        fun formatTime(timeInMillis: Long): String {
            var millis = timeInMillis
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            millis -= TimeUnit.HOURS.toMillis(hours)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
            millis -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }

        fun formatDistance(value: Long): String {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.DOWN
            return df.format(value / 1000.0)
        }

        fun formatSpeed(speedInMS: Int): String {
            val df = DecimalFormat("#")
            df.roundingMode = RoundingMode.DOWN
            val speed = speedInMS * 3.6
            return df.format(speed)
        }
    }
}