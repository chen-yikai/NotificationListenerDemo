package dev.eliaschen.notificationlistener.services

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import dev.eliaschen.notificationlistener.drawableToBitmap
import dev.eliaschen.notificationlistener.room.NotificationDao
import dev.eliaschen.notificationlistener.room.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var notificationDao: NotificationDao

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Log.i("notification_listener", "the notification listener service has fire up")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notification = sbn.notification
        val extra = notification.extras
        val smallIcon = notification.smallIcon
        val smallBitmap = smallIcon?.loadDrawable(this)?.let { drawable ->
            drawableToBitmap(drawable)
        }
        val title = extra?.getString(Notification.EXTRA_TITLE)
        val text = extra?.getString(Notification.EXTRA_TEXT)
        val data = NotificationEntity(
            notificationId = sbn.id,
            title = title!!,
            text = text!!,
            icon = smallBitmap!!,
            packageName = sbn.packageName,
            timestamp = System.currentTimeMillis()
        )
        serviceScope.launch {
            if (!sbn.isOngoing) {
                notificationDao.addNotification(data)
                Log.i("notification_listener", "New Notification: $title")
                cancelNotification(sbn.key)
            }
        }
    }
}