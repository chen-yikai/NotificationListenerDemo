package dev.eliaschen.notificationlistener.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.collection.LruCache
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.eliaschen.notificationlistener.notification_label_channel
import dev.eliaschen.notificationlistener.room.NotificationDao
import dev.eliaschen.notificationlistener.room.NotificationEntity
import dev.eliaschen.notificationlistener.util.drawableToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var cache: LruCache<Long, PendingIntent>

    @Inject
    lateinit var notifyManager: NotificationManager
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var notificationCount = 0

    companion object {
        var notificationCount = 0
    }

    override fun onCreate() {
        super.onCreate()
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
                val id = notificationDao.addNotification(data)
                cache.put(id, sbn.notification.contentIntent)
                cancelNotification(sbn.key)
            }
        }
    }

    private fun displayStatusNotification() {
        serviceScope.launch {
            notificationCount = notificationDao.getAllNotifications().count()
        }
        val builder = NotificationCompat.Builder(this, notification_label_channel)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Active Notifications")
            .setContentText("You have $notificationCount active notifications")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true).build()
        notifyManager.notify(1001, builder)
    }
}