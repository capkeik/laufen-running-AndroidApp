package com.example.laufen.training.presentation.training

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laufen.R
import com.example.laufen.training.permission.PermissionState
import com.example.laufen.training.presentation.PermissionUI
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.maps.android.compose.*
import dagger.hilt.android.lifecycle.HiltViewModel

private const val TAG = "training_screen"

@Composable
fun TrainingScreen() {
    val viewModel: TrainingViewModel = hiltViewModel()
    val context = LocalContext.current
    val isTracking = viewModel.isTracking.collectAsState()
    val curPosition = viewModel.curPosition.collectAsState()
    val polylines = viewModel.polylines.collectAsState()
    var isGranted = false
    val trainingInfoState = viewModel.trainingInfoState.collectAsState()

    PermissionUI(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
        stringResource(id = R.string.permission_location_rationale),
        scaffoldState = rememberScaffoldState()
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionState.OnPermissionGranted -> {
                Log.d(TAG, "Permission grant successful")
                isGranted = true
                if (viewModel.isLocated) {
                    viewModel.initLocation(context)
                }
            }
            is PermissionState.OnPermissionDenied -> {
                Log.d(TAG, "Permission grant denied")
            }
        }
    }

    if (isGranted) {

        val cameraPositionState =
            CameraPositionState(CameraPosition.fromLatLngZoom(curPosition.value, 15f))

        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember { mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            ))
        }


        Column {
            TrainingInfo(trainingInfoState.value)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = mapProperties,
            ) {
                for (p in polylines.value) {
                    Polyline(
                        points = p,
                        jointType = JointType.ROUND,
                        color = Color.Red
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (!isTracking.value) {
                ActionButton(
                    color = Color.Black,
                    text = if (trainingInfoState.value.duration == 0L) "start" else "resume",
                    isMain = true
                ) {
                    if (isGranted) {
                        viewModel.startTracking(context)
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    ActionButton(
                        color = Color(0xFFDB9000),
                        text = "pause",
                        isMain = false
                    ) {
                        if (isGranted) {
                            viewModel.pauseTracking(context)
                        }
                    }
                    ActionButton(
                        color = Color(0xFFB11226),
                        text = "stop",
                        isMain = false
                    ) {
                        if (isGranted) {
                            viewModel.saveTrainingToDB()
                            Log.d(TAG, "run saved successfullyss")
                            viewModel.stopTracking(context)
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { viewModel.stopTracking(context) },
                    enabled = isTracking.value,
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Cansel training")
                }
            }
        }
    }
}