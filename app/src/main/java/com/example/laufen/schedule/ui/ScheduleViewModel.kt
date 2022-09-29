package com.example.laufen.schedule.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laufen.schedule.data.ScheduleRepository
import com.example.laufen.schedule.data.ScheduleRepositoryPrefs
import com.example.laufen.schedule.data.Scheduler
import com.example.laufen.schedule.data.entity.ScheduleEntity
import com.example.laufen.schedule.data.initMillis

data class ScheduleViewState(
    val schedule: List<ScheduleEntity> = listOf()
)

class ScheduleViewModel : ViewModel() {
    private lateinit var scheduleRepository: ScheduleRepository
    private val _viewState: MutableLiveData<ScheduleViewState> = MutableLiveData(ScheduleViewState())
    val viewState: LiveData<ScheduleViewState> get() = _viewState

    private fun addToState(scheduleToAdd: List<ScheduleEntity>) {
        val newList: MutableList<ScheduleEntity> = viewState
            .value?.schedule?.toMutableList() ?: mutableListOf()
        newList += scheduleToAdd
        putToState(newList)
    }

    private fun putToState(newList: List<ScheduleEntity>) {
        newList.toMutableList().sortWith(
            compareBy<ScheduleEntity>
            { it.dayOfWeek }.thenBy
            { it.hour }.thenBy
            { it.minute }
        )
        newList.distinct()
        _viewState.postValue(
            viewState.value?.copy(
                schedule = newList
            )
        )
    }

    fun initRepo(context: Context) {
        scheduleRepository = ScheduleRepositoryPrefs(context)
        _viewState.postValue(
            viewState.value?.copy(
                schedule = scheduleRepository.getAlarms()
            )
        )
    }

    fun addSchedule(hour: Int, minute: Int, daysOfWeek: List<Int>, context: Context) {
        val newScheduleElements = List(daysOfWeek.size) {
            ScheduleEntity(hour, minute, it)
        }
        newScheduleElements.forEach() {
            it.initMillis()
        }
        addToState(newScheduleElements)
        scheduleRepository.addAlarms(newScheduleElements)
        updateAlarms(context)
    }

    fun deleteAlarm(schedule: ScheduleEntity, context: Context) {
        val oldList = _viewState.value?.schedule ?: listOf()
        val newList = oldList.toMutableList()
        newList.remove(schedule)
        putToState(newList)
        scheduleRepository.removeAlarm(schedule)
        updateAlarms(context)
    }

    private fun updateAlarms(context: Context) {
        Scheduler.deleteAlarms(context)
        val next = scheduleRepository.getNextAlarm()
        next?.let {
            Scheduler.setSingleAlarm(context, it.millis)
        }
    }
}
