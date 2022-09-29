package com.example.laufen.schedule.data.entity

data class ScheduleEntity(
    val hour: Int,
    val minute: Int,
    val dayOfWeek: Int,
    var millis: Long = 0
) {
    override fun equals(other: Any?): Boolean {
        return other is ScheduleEntity &&
                other.hour == hour &&
                other.dayOfWeek == dayOfWeek &&
                other.minute == minute
    }
}
