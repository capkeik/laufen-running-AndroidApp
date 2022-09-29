package com.example.laufen.navigation

import com.example.laufen.R

sealed class BottomNavItem(val title: String, var icon:Int, var route:String,) {
    object Activity: BottomNavItem(
        "Activity",
        R.drawable.ic_round_directions_run_24,
        "activity")
    object Progress: BottomNavItem(
        "History",
        R.drawable.ic_round_grading_24,
        "progress")
    object Schedule: BottomNavItem(
        "Schedule",
        R.drawable.ic_round_notifications_none_24,
        "schedule")
}
