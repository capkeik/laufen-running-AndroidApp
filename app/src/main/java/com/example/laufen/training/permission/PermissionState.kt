package com.example.laufen.training.permission

sealed class PermissionState {

    object OnPermissionGranted : PermissionState()

    object OnPermissionDenied : PermissionState()
}