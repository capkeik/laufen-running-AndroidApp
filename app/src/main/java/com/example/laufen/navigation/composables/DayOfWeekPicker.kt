package com.example.laufen.navigation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.laufen.training.Const.daysOfWeek

@Composable
fun DayOfWeekPicker(
    onDowClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val items = daysOfWeek
            Row() {
                items.forEachIndexed { index, s ->
                    DayOfWeekButton(
                        name = s,
                        onClick = { onDowClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun DayOfWeekButton(
    name: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.size(50.dp, 40.dp),
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = name.first().toString(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
    }
}

@Composable
@Preview
fun DowPrev() {
    DayOfWeekButton(name = "Sunday",) {}
}
