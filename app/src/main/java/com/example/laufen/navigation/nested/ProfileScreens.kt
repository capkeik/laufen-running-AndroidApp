package com.example.laufen.navigation.nested

sealed class ProfileScreens(val route: String) {
    object Profile : ProfileScreens("profile")
}
