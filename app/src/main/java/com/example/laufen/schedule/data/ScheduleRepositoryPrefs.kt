package com.example.laufen.schedule.data

import android.content.Context
import com.example.laufen.schedule.data.entity.ScheduleEntity
import com.example.laufen.training.Const
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScheduleRepositoryPrefs(
    private val context: Context
) : ScheduleRepository {

    private val sharedPreferences = context.getSharedPreferences(
        Const.SCHEDULE_PREFS_FILE,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    override fun getAlarms(): List<ScheduleEntity> = getAlarmsList()

    override fun getNextAlarm(): ScheduleEntity? {
        val lst = getAlarmsList()
        val current = if (lst.isNotEmpty()) lst[0] else null
        lst.remove(current)
        current?.addWeek()
        current?.let { lst.add(it) }
        return if (lst.isNotEmpty()) lst[0] else null
    }

    override fun addAlarms(plans: List<ScheduleEntity>) {
        val oldList = getAlarmsList()
        val newList = oldList + plans
        putSchedule(newList)
    }

    override fun removeAlarm(planToRemove: ScheduleEntity) {
        val newList = getAlarmsList()
        newList.remove(planToRemove)
        putSchedule(newList)
    }

    private fun getAlarmsList(): MutableList<ScheduleEntity> {
        val listString: String = sharedPreferences
            .getString(Const.SCHEDULE_PREFS_TAG, "[]") ?: "[]"
        val parseType = object : TypeToken<MutableList<ScheduleEntity>>() {}.type
        return gson.fromJson(listString, parseType)
    }

    private fun putSchedule(scheduleList: List<ScheduleEntity>) {
        val newList = scheduleList.distinct().sortedBy { it.millis }
        val newListString = gson.toJson(newList)
        sharedPreferences.edit()
            .putString(
                Const.SCHEDULE_PREFS_TAG,
                newListString
            ).apply()
    }
}
