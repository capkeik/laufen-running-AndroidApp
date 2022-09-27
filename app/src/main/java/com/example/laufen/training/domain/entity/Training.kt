package com.example.laufen.training.domain.entity

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class Training(
    val timestamp: Long,
    val duration: Long,
    val distance: Long,
    val burnedCalories: Int,
    val route: List<LatLng>,
    @PrimaryKey val  id: Int? = null
)
