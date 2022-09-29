package com.example.laufen.training.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laufen.training.domain.entity.Training
import com.example.laufen.training.domain.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
): ViewModel() {
    private val _list = MutableStateFlow<List<Training>>(listOf())
    val list = _list.asStateFlow()

    fun deleteTraining(training: Training) {
        _list.update {
            val newList = it.toMutableList()
            newList.remove(training)
            newList
        }
        viewModelScope.launch {
            trainingRepository.deleteTraining(training)
        }
    }


    fun getTrainingsList() {
        viewModelScope.launch {
            _list.value = trainingRepository.getTrainingList()
        }
    }
}