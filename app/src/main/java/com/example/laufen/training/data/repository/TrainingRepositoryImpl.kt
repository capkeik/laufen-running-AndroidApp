package com.example.laufen.training.data.repository

import com.example.laufen.training.data.data_source.room.TrainingDatabase
import com.example.laufen.training.domain.entity.Training
import com.example.laufen.training.domain.repository.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingDatabase: TrainingDatabase
) : TrainingRepository {
    override suspend fun getTrainingList(): List<Training> {
        return trainingDatabase.trainingDao.getTrainings()
    }

    override suspend fun addTraining(training: Training) {
        trainingDatabase.trainingDao.insertTraining(training)
    }

    override suspend fun deleteTraining(training: Training?) {
        trainingDatabase.trainingDao.deleteTraining(training)
    }
}