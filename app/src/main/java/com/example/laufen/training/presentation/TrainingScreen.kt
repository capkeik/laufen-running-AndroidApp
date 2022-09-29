package com.example.laufen.training.presentation

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laufen.R
import com.example.laufen.training.Const.ACTION_START_OR_RESUME
import com.example.laufen.training.permission.PermissionState
import com.example.laufen.training.service.Polyline
import com.example.laufen.training.utils.Common
import com.example.laufen.training.utils.Formatter
import com.example.laufen.ui.theme.LaufenTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private const val TAG = "training_screen"

@Composable
fun TrainingScreen() {
    val viewModel: TrainingViewModel = viewModel()
    val context = LocalContext.current
    val curPosition = viewModel.curPosition.collectAsState()
    val updateCounter = viewModel.updateCounter.collectAsState()
    val polylines = viewModel.polylines.collectAsState()
    val isGranted = viewModel.isPermissinoGranted.collectAsState()

    PermissionUI(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
        stringResource(id = R.string.permission_location_rationale),
        scaffoldState = rememberScaffoldState()
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionState.OnPermissionGranted -> {
                Log.d(TAG, "Permission grant successful")
                viewModel.grant()
            }
            is PermissionState.OnPermissionDenied -> {
                Log.d(TAG, "Permission grant denied")
            }
        }
    }

    if (isGranted.value) {
        LaunchedEffect(Unit) {
            if (!viewModel.isServiceInited && isGranted.value) {
                viewModel.sendServiceCommand(
                    action = ACTION_START_OR_RESUME,
                    context
                )
            }
        }

        val cameraPositionState =
            CameraPositionState(CameraPosition.fromLatLngZoom(curPosition.value, 15f))

        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }


        Column {
            Text(
                text = "${curPosition.value} / ${updateCounter.value}"
            )
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
    }
}

@Composable
fun TrainingInfo(trainingInfoState: TrainingInfoState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp)
            .padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Formatter.formatTime(trainingInfoState.duration),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = "Duration",
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    value = Formatter.formatDistance(
                        trainingInfoState.distance
                    ),
                    name = "Distance (km)"
                )
                InfoItem(
                    value = Formatter.formatSpeed(
                        trainingInfoState.avgSpeed
                    ),
                    name = "Avg. speed (km/h)"
                )
                InfoItem(
                    value = trainingInfoState.calories.toString(),
                    name = "Calories (Cal)"
                )
            }

        }
    }
}

@Preview
@Composable
fun TrainingInfoPrev() {
    TrainingInfo(
        trainingInfoState = TrainingInfoState(
            duration = 4382749,
            distance = 15937,
            avgSpeed = 2,
            calories = 673,
        )
    )
}

@Composable
fun InfoItem(
    value: String,
    name: String
) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}