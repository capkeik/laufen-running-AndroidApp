package com.example.laufen.schedule.data

import com.example.laufen.schedule.data.entity.ScheduleEntity

interface ScheduleRepository {
    fun getNextAlarm(): ScheduleEntity?
    fun addAlarms(plans: List<ScheduleEntity>)
    fun removeAlarm(planToRemove: ScheduleEntity)
    fun getAlarms(): List<ScheduleEntity>
}
