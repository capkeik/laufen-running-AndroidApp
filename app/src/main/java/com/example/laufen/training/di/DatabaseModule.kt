package com.example.laufen.training.di

import android.content.Context
import androidx.room.Room
import com.example.laufen.training.data.data_source.room.TrainingDao
import com.example.laufen.training.data.data_source.room.TrainingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideDatabase(trainingDatabase: TrainingDatabase): TrainingDao {
        return trainingDatabase.trainingDao
    }

    @Provides
    fun provideTrainingDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        TrainingDatabase::class.java,
        TrainingDatabase.name
    ).build()
}
