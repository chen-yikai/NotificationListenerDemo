package dev.eliaschen.noti.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.collection.LruCache
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.eliaschen.noti.MainActivity
import dev.eliaschen.noti.notification_label_channel
import dev.eliaschen.noti.room.dao.NotificationDao
import dev.eliaschen.noti.room.table.NotificationEntity
import dev.eliaschen.noti.utils.createNotificationIcon
import dev.eliaschen.noti.utils.drawableToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

// ian's here
@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var notificationDao: NotificationDao

    @Inject
    lateinit var cache: LruCache<Long, PendingIntent>

    @Inject
    lateinit var notifyManager: NotificationManager

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var lastNotification: Notification? = null

    override fun onCreate() {
        super.onCreate()
        observeNotificationCount()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        if (sbn.id == 1001 && sbn.packageName == packageName) {
            observeNotificationCount()
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        if (sbn.packageName == packageName) return
        val notification = sbn.notification
        
        // Skip media/music notifications to avoid stopping playback
        if (notification.extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
            return
        }
        
        val extra = notification.extras
        val smallIcon = notification.smallIcon
        val smallBitmap = smallIcon?.loadDrawable(this)?.let { drawable ->
            drawableToBitmap(drawable)
        }
        val title = extra.getCharSequence("android.title")?.toString() ?: return
        val text = extra.getCharSequence("android.text")?.toString() ?: ""
        val data = NotificationEntity(
            notificationId = sbn.id,
            title = title,
            text = text,
            icon = smallBitmap!!,
            packageName = sbn.packageName,
            timestamp = System.currentTimeMillis()
        )
        if (!sbn.isOngoing) {
            serviceScope.launch {
                val duplicateCount = notificationDao.isDuplicateNotification(
                    packageName = data.packageName,
                    title = data.title,
                    text = data.text
                )
                if (duplicateCount == 0) {
                    val id = notificationDao.addNotification(data)
                    cache.put(id, sbn.notification.contentIntent)
                }
            }
            cancelNotification(sbn.key)
        }
    }

    private fun observeNotificationCount() {
        serviceScope.launch {
            notificationDao.getActiveNotifications().collect { notifications ->
                val notificationCount = notifications.size
                val intent = Intent(this@NotificationListener, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingIntent = PendingIntent.getActivity(
                    this@NotificationListener,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val icon = createNotificationIcon(appContext, notificationCount)
                val notification =
                    NotificationCompat.Builder(
                        this@NotificationListener,
                        notification_label_channel
                    )
                        .setSmallIcon(icon)
                        .setContentTitle(if (notificationCount > 0) ("$notificationCount ${if (notificationCount == 1) "Notification" else "Notifications"}") else "You don't have any notification yet")
                        .setContentText("Tap to see them")
                        .setNumber(notificationCount)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setAutoCancel(false)
                        .setSound(null)
                        .setVibrate(null)
                        .setSilent(true)
                        .build()
                lastNotification = notification
                startForeground(
                    1001,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            }
        }
    }
}