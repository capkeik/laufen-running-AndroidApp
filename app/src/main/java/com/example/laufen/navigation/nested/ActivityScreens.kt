package com.example.laufen.navigation.nested

sealed class ActivityScreens(val route: String) {
    object Activity: ActivityScreens("activity")
    object Training: ActivityScreens("training")
}
