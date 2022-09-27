package com.example.laufen.training.presentation

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laufen.R
import com.example.laufen.training.permission.PermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private const val TAG = "training_screen"
@Composable
fun TrainingScreen() {
    val viewModel: TrainingViewModel = viewModel()
    val context = LocalContext.current
    val currentPosition = viewModel.currentPosition.collectAsState()
    val trainingState = viewModel.trainingState.collectAsState()
    val cameraPositionState = CameraPositionState(
        position = CameraPosition.fromLatLngZoom(currentPosition.value, 10f)
    )

    LaunchedEffect(key1 = Unit) {
        if (!viewModel.isProviderInitialised) {
            viewModel.initLocation(context)
        }
    }

    PermissionUI(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
        stringResource(id = R.string.permission_location_rationale),
        scaffoldState = rememberScaffoldState()
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionState.OnPermissionGranted -> {
                Log.d(TAG, "Permission grant successful")
            }
            is PermissionState.OnPermissionDenied -> {
                Log.d(TAG, "Permission grant denied")
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = currentPosition.value!!),
            title = "Singapore"
        )
    }

    Column() {
        Text(currentPosition.value.toString())
    }
}