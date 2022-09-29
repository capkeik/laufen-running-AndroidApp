package com.example.laufen.training.presentation.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laufen.training.utils.Formatter

@Composable
fun TrainingInfo(trainingInfoState: TrainingInfoState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp),
        elevation = 4.dp
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

@Composable
fun ActionButton(
    color: Color,
    text: String,
    isMain: Boolean = false,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .height(56.dp)
            .width(if (isMain) 256.dp else 180.dp)
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(100.dp),
        backgroundColor = color,
        contentColor = Color.White,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text.uppercase(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        }
    }
}


@Preview
@Composable
fun TrainingInfoPrev() {
    TrainingInfo(
        trainingInfoState = TrainingInfoState(
            duration = 4382749,
            distance = 27,
            avgSpeed = 2,
            calories = 673,
        )
    )
}

@Preview
@Composable
fun ActionButtonPrev() {
    ActionButton(
        color = Color.Black,
        text = "start",
        isMain = true,
    ) { }
}