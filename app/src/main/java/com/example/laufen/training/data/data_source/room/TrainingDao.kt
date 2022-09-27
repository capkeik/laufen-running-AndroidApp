package com.example.laufen.training.data.data_source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laufen.training.domain.entity.Training

@Dao
interface TrainingDao {
    @Query("SELECT * FROM training")
    suspend fun getTrainings(): List<Training>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: Training)

    @Delete
    suspend fun deleteTraining(training: Training?)
}