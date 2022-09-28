package com.example.laufen.training.presentation

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laufen.R
import com.example.laufen.training.Const.ACTION_START_OR_RESUME
import com.example.laufen.training.permission.PermissionState
import com.example.laufen.training.service.Polyline
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private const val TAG = "training_screen"
@Composable
fun TrainingScreen() {
    val viewModel: TrainingViewModel = viewModel()
    val context = LocalContext.current
    val distance = viewModel.curDistance.collectAsState()
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


        Column() {
            Text(
                text = "${curPosition.value} / ${updateCounter.value}"
            )
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
                properties = mapProperties,
            ) {
                for (p in listOf<List<LatLng>>(
                    listOf(
                        LatLng(55.79328781735486, 49.12508607707867),
                        LatLng(55.793782400477795, 49.12514508567111),
                        LatLng(55.79434935389224, 49.12528992494345),
                        LatLng(55.794479028247544, 49.125300653778446),
                        LatLng(55.794587592491986, 49.125150450088604)
                    ),
                    listOf(
                        LatLng(55.79361351914194, 49.1241204819297),
                        LatLng(55.79423777328747, 49.123160251198236),
                        LatLng(55.79383969933607, 49.12167967196982)
                    )
                )) {
                    Polyline(
                        points = p,
                        jointType = JointType.ROUND
                    )
                }
            }
        }
    }
}

@Composable
fun Map(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    uiSettings: MapUiSettings,
    mapProperties: MapProperties
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = mapProperties,
    ) {
        for (p in listOf<List<LatLng>>(
            listOf(
                LatLng(55.79328781735486, 49.12508607707867),
                LatLng(55.793782400477795, 49.12514508567111),
                LatLng(55.79434935389224, 49.12528992494345),
                LatLng(55.794479028247544, 49.125300653778446),
                LatLng(55.794587592491986, 49.125150450088604)
            ),
            listOf(
                LatLng(55.79361351914194, 49.1241204819297),
                LatLng(55.79423777328747, 49.123160251198236),
                LatLng(55.79383969933607, 49.12167967196982)
            )
        )) {
            Polyline(
                points = p,
                jointType = JointType.ROUND
            )
        }
    }
}