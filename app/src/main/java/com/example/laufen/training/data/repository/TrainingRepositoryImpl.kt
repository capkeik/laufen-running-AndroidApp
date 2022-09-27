package com.example.laufen.training.data.repository

import com.example.laufen.training.data.data_source.room.TrainingDao
import com.example.laufen.training.domain.entity.Training
import com.example.laufen.training.domain.repository.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingDao: TrainingDao
) : TrainingRepository {
    override suspend fun getTrainingList(): List<Training> {
        return trainingDao.getTrainings()
    }

    override suspend fun addTraining(training: Training) {
        trainingDao.insertTraining(training)
    }

    override suspend fun deleteTraining(training: Training?) {
        trainingDao.deleteTraining(training)
    }
}