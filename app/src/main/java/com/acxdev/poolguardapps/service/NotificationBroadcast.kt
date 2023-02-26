package com.acxdev.poolguardapps.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.acxdev.poolguardapps.R
import com.acxdev.poolguardapps.common.Constant
import com.acxdev.poolguardapps.service.NotificationChannel.notificationChannel
import com.acxdev.poolguardapps.util.getSavedPrefs
import com.acxdev.poolguardapps.ui.main.ActivityMain

class NotificationBroadcast: BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        context.notificationChannel()
    }

    companion object {

        fun Context.showNotification(
            channelId: String,
            channelName: String,
            title: String,
            body: String
        ) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val sound: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.tone)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
                val attributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                channel.setShowBadge(true)
                channel.enableLights(true)
                channel.enableVibration(true)
                channel.setSound(sound, attributes)
            }
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo_small)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addNextIntent(Intent(this, ActivityMain::class.java))
            val pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.setContentIntent(pendingIntent)
            notificationManager.notify(random(), builder.build())
        }

        private fun random(): Int = (1..1000).random()

        fun Context.startAutoCheck() {
            val intent = Intent(this, NotificationBroadcast::class.java)
            intent.action = Constant.BACKGROUND_PROCESS
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), getSavedPrefs(Constant.SharedPrefs.REFRESH_PERIOD).toString().toLong(), pendingIntent)
        }

        fun Context.stopAutoCheck(){
            Intent(this, NotificationBroadcast::class.java)
                .also {
                    stopService(it)
                }
        }

        fun Context.createNotificationChannel() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(Constant.CHANNEL_ID_ANNOUNCEMENT, Constant.ANNOUNCEMENT, NotificationManager.IMPORTANCE_HIGH)
                channel.description = Constant.ANNOUNCEMENT_DESCRIPTION
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}