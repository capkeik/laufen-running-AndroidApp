package com.example.laufen.navigation.nested

sealed class ProgressScreens(val route: String) {
    object Progress : ProgressScreens("progress")
    object Schedule : ProgressScreens("schedule")
}
