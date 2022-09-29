package com.example.laufen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laufen.navigation.nested.ActivityScreens
import com.example.laufen.navigation.nested.ProgressScreens
import com.example.laufen.schedule.ui.ProgressScreen
import com.example.laufen.schedule.ui.ScheduleScreen
import com.example.laufen.training.presentation.training.TrainingScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ActivityScreens.Training.route) {
            TrainingScreen()
        }
        composable(ActivityScreens.Activity.route) {
            TrainingScreen()
        }
        composable(ProgressScreens.Progress.route) {
            ProgressScreen(navController)
        }
        composable(ProgressScreens.Schedule.route) {
            ScheduleScreen()
        }
    }
}
