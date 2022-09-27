package com.example.laufen.training.domain.repository

import com.example.laufen.training.domain.entity.Training

interface TrainingRepository {
    suspend fun getTrainingList(): List<Training>

    suspend fun addTraining(training: Training)

    suspend fun deleteTraining(training: Training?)
}