package com.example.laufen.training

object Const {
    const val SCHEDULE_PREFS_FILE = "SCHEDULE_PREFS_FILE"
    const val SCHEDULE_PREFS_TAG = "SCHEDULE_PREFS_TAG"

    const val ACTION_SHOW_TRAINING_SCREEN = "ACTION_SHOW_TRAINING_SCREEN"

    const val ACTION_START_OR_RESUME = "ACTION_START_OR_RESUME"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_STOP = "ACTION_STOP"

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val TIMER_UPDATE_INTERVAL = 50L

    val daysOfWeek = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
}