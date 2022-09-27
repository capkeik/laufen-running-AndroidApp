package com.example.laufen.training.data.data_source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.laufen.training.domain.entity.Training

@Database(
    entities = [Training::class],
    version = 1
)
abstract class TrainingDatabase: RoomDatabase() {
    abstract val trainingDao: TrainingDao

    companion object {
        val name = "training_database"
    }
}
