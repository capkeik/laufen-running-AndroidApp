package com.example.laufen.training.domain.entity

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Training(
    val timestamp: Long,
    val duration: Long,
    val distance: Long,
    val burnedCalories: Int,
    val route: List<Location>,
    @PrimaryKey val  id: Int? = null
)
