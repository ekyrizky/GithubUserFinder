package com.ekyrizky.githubezfinder.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ekyrizky.githubezfinder.R
import com.ekyrizky.githubezfinder.view.HomeActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val ID_REPEATING = 100
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        showAlarmNotification(context)
    }

    private fun showAlarmNotification(context: Context) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Remidner_channel"

        val title = context.getString(R.string.app_name)
        val message = context.getString(R.string.find_user)

        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
            .addParentStack(HomeActivity::class.java)
            .addNextIntent(intent)
            .getPendingIntent(ID_REPEATING, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_white_24dp)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING, notification)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)



        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, context.getString(R.string.alarm_enabled), Toast.LENGTH_SHORT).show()
    }

    fun setCancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, context.getString(R.string.alarm_canceled), Toast.LENGTH_SHORT).show()
    }
}