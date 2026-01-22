package dev.eliaschen.notificationlistener

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

const val notification_label_channel = "notification_label_channel"
const val notification_repost_channel = "notification_repost_channel"

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val label = NotificationChannel(
            notification_label_channel, "Notification Label",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)
            enableVibration(false)
        }
        val repost = NotificationChannel(
            notification_repost_channel, "Notification Repost",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannels(listOf(label, repost))
    }
}