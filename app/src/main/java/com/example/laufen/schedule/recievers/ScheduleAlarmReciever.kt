package com.example.laufen.schedule.recievers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.laufen.R
import com.example.laufen.schedule.data.ScheduleRepositoryPrefs
import com.example.laufen.schedule.data.Scheduler

class ScheduleAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val repository = ScheduleRepositoryPrefs(context)
        val next = repository.getNextAlarm()
        next?.let{
            Scheduler.setSingleAlarm(context, it.millis)
        }

        showNotification(context, "БЕГИ, СУКА, БЕГИИИИИ!!!!!!!", "пора на треню")
    }
}

private fun showNotification(context: Context, title: String, desc: String) {
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "training-alarm-channel"
    val channelName = "training-alarm"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(desc)
        .setSmallIcon(R.drawable.ic_round_directions_run_24)

    manager.notify(1, builder.build())

}
