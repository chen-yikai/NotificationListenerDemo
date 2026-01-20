package dev.eliaschen.notificationlistener.services

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {
    override fun onCreate() {
        super.onCreate()
        Log.i("notification_listener", "the notification listener service has fire up")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notification = sbn.notification
        val extra = notification.extras
        val icon = notification.smallIcon
        val title = extra?.getString(Notification.EXTRA_TITLE)
        Log.i("notification_listener", "New Notification: $title")
    }
}