package com.example.laufen.training.di

import com.example.laufen.training.data.repository.TrainingRepositoryImpl
import com.example.laufen.training.domain.repository.TrainingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun provideTrainingRepository(repositoryImpl: TrainingRepositoryImpl): TrainingRepository
}