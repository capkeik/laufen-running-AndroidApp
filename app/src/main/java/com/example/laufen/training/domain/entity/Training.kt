package com.example.laufen.training.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Training(
    val timestamp: Long,
    val duration: Long,
    val distance: Long,
    val burnedCalories: Int,
    @PrimaryKey val  id: Int? = null
)
