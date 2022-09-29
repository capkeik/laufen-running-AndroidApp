package com.example.laufen.schedule.data

import com.example.laufen.schedule.data.entity.ScheduleEntity
import com.example.laufen.training.Const.daysOfWeek
import java.util.Calendar

fun ScheduleEntity.initMillis() {
    val calendar = Calendar.getInstance()
    with(calendar) {
        set(Calendar.DAY_OF_WEEK, this@initMillis.dayOfWeek)
        set(Calendar.HOUR_OF_DAY, this@initMillis.hour)
        set(Calendar.MINUTE, this@initMillis.minute)
    }
    this.millis = calendar.timeInMillis
}

fun ScheduleEntity.addWeek() {
    val calendar = Calendar.getInstance()
    with(calendar) {
        set(Calendar.DAY_OF_WEEK, this@addWeek.dayOfWeek)
        set(Calendar.HOUR_OF_DAY, this@addWeek.hour)
        set(Calendar.MINUTE, this@addWeek.minute)
        add(Calendar.WEEK_OF_YEAR, 1)
    }
    this.millis = calendar.timeInMillis
}

fun ScheduleEntity.format(): String {
    val sHour = if (hour > 9) hour.toString() else "0$hour"
    val sMinute = if (minute > 9) minute.toString() else "0$minute"
    val sDayOfWeek = daysOfWeek[dayOfWeek]
    return "$sHour:$sMinute   $sDayOfWeek"
}
