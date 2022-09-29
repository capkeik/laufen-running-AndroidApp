package com.example.laufen.schedule.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laufen.schedule.data.entity.ScheduleEntity
import com.example.laufen.schedule.data.format
import com.example.laufen.navigation.composables.DayOfWeekPicker
import com.example.laufen.ui.theme.Shapes
import java.util.*

@Composable
fun ScheduleScreen() {

    val viewModel: ScheduleViewModel = viewModel()
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    var hourOfDay = calendar[Calendar.HOUR_OF_DAY]
    var minute = calendar[Calendar.MINUTE]
    var dayOfWeek = 0

    val openDialog = remember { mutableStateOf(false) }

    val viewState: State<ScheduleViewState> = viewModel.viewState.observeAsState(ScheduleViewState())
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, mMinute: Int ->
            hourOfDay = hour
            minute = mMinute
            openDialog.value = true
        }, hourOfDay, minute, false
    )

    LaunchedEffect("initScheduleRepo") { viewModel.initRepo(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Schedule",
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.padding(8.dp))
        if (openDialog.value) {
            DayOfWeekPicker(onDowClick = {
                dayOfWeek = it
                viewModel.addSchedule(hourOfDay, minute, listOf(dayOfWeek), context)
                openDialog.value = false
            }) {
                openDialog.value = false
            }
        }
        Button(
            onClick = { timePickerDialog.show() }
        ) {
            Text(text = "Schedule Training", color = Color.White)
        }
        LazyColumn {
            items(viewState.value.schedule) { item: ScheduleEntity ->
                ScheduleItem(
                    item = item,
                ) {
                    viewModel.deleteAlarm(item, context)
                }
            }
        }
        Spacer(modifier = Modifier.size(100.dp))
    }
}

@Composable
fun ScheduleItem(
    item: ScheduleEntity,
    onClick: () -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp),
        shape = Shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.format())
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(id = com.example.laufen.R.drawable.ic_round_delete_outline_24),
                    contentDescription = "Delete button"
                )
            }
        }
    }
}

@Composable
@Preview
fun ScheduleItemPreview() {
    ScheduleItem(
        ScheduleEntity(
            0,
            0,
            0
        )
    ) { }
}
