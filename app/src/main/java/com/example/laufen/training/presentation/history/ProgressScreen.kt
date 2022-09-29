package com.example.laufen.training.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.laufen.R
import com.example.laufen.training.domain.entity.Training
import com.example.laufen.training.utils.Formatter
import com.example.laufen.ui.theme.Shapes
import java.sql.Date
import java.sql.Timestamp

@Composable
fun ProgressScreen() {
    val viewModel = hiltViewModel<HistoryViewModel>()

    val list = viewModel.list.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getTrainingsList()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trainings history",
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp
        )
        LazyColumn() {
            items(list.value) { item ->
                HistoryListItem(
                    item,
                    onClick = { viewModel.deleteTraining(item) }
                )
            }
        }
    }
}

@Composable
fun HistoryListItem(
    item: Training,
    onClick: () -> Unit)
{
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
            Column{
                val timestamp = Timestamp(item.timestamp)
                val date = Date(timestamp.time).toString()

                Text(
                    text = Formatter.formatDistance(item.distance) + " km",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = date,
                    fontSize = 13.sp
                )
            }
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_delete_outline_24),
                    contentDescription = "Delete button"
                )
            }
        }
    }
}
