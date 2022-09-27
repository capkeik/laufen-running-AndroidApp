package com.example.laufen.training.presentation

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.example.laufen.training.permission.PermissionState
import com.example.laufen.training.utils.Common

private const val TAG = "PermissionUI"

@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionRationale: String,
    scaffoldState: ScaffoldState,
    permissionAction: (PermissionState) -> Unit
) {


    val permissionGranted =
        Common.checkIfPermissionGranted(
            context,
            permission
        )

    if (permissionGranted) {
        Log.d(TAG, "Permission already granted, exiting..")
        permissionAction(PermissionState.OnPermissionGranted)
        return
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permission provided by user")
            // Permission Accepted
            permissionAction(PermissionState.OnPermissionGranted)
        } else {
            Log.d(TAG, "Permission denied by user")
            // Permission Denied
            permissionAction(PermissionState.OnPermissionDenied)
        }
    }


    val showPermissionRationale = Common.shouldShowPermissionRationale(
        context,
        permission
    )


    if (showPermissionRationale) {
        Log.d(TAG, "Showing permission rationale for $permission")

        LaunchedEffect(showPermissionRationale) {

            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long

            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d(TAG, "User dismissed permission rationale for $permission")
                    //User denied the permission, do nothing
                    permissionAction(PermissionState.OnPermissionDenied)
                }
                SnackbarResult.ActionPerformed -> {
                    Log.d(TAG, "User granted permission for $permission rationale. Launching permission request..")
                    launcher.launch(permission)
                }
            }
        }
    } else {
        //Request permissions again
        Log.d(TAG, "Requesting permission for $permission")
        SideEffect {
            launcher.launch(permission)
        }
    }
}
