package com.example.laufen.training.domain.entity

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.laufen.training.service.Polylines
import com.google.android.gms.maps.model.LatLng

@Entity
data class Training(
    val timestamp: Long,
    val duration: Long,
    val distance: Long,
    val burnedCalories: Int,
    val route: Polylines,
    @PrimaryKey val  id: Int? = null
)
