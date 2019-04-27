package com.hackathon.lib.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hackathon.Constants
import com.hackathon.R

object Notify {
    fun send(context: Context, title: String, content: String) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = notificationBuilder(context, notificationManager, title, content)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun notificationBuilder(context: Context, notificationManager: NotificationManager, title: String, content: String, channel: String? = null): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= 26) {
            val channelName = channel ?: context.getString(R.string.app_name)

            val mChannel = NotificationChannel(Constants.Notification.CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }

        return NotificationCompat.Builder(context, Constants.Notification.ID)
                .setSmallIcon(R.drawable.ic_small_icon)
                .setAutoCancel(true)
                .setChannelId(Constants.Notification.CHANNEL_ID)
                .setNumber(1)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(NotificationCompat.BigTextStyle())
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
    }
}