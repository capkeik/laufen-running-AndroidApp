package com.example.laufen.schedule.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.laufen.schedule.recievers.ScheduleAlarmReceiver


object Scheduler {
    fun setSingleAlarm(
        context: Context,
        millis: Long
    ) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ScheduleAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun deleteAlarms(context: Context) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ScheduleAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}
