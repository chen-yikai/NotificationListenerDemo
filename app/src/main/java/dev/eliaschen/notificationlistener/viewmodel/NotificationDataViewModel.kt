package dev.eliaschen.notificationlistener.viewmodel

import android.app.PendingIntent
import androidx.collection.LruCache
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.eliaschen.notificationlistener.notification_repost_channel
import dev.eliaschen.notificationlistener.room.NotificationDao
import dev.eliaschen.notificationlistener.room.NotificationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val db: NotificationDao,
    val cache: LruCache<Long, PendingIntent>,
    private val notifyManager: NotificationManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    val notifications: StateFlow<List<NotificationEntity>> = db.getActiveNotifications().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            db.deleteNotification(notification)
        }
    }

    fun repostNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            val icon = IconCompat.createWithBitmap(notification.icon)
            val uniqueKey = "Channel_Repost_Group_" + System.currentTimeMillis()
            val builder = NotificationCompat.Builder(appContext, notification_repost_channel)
                .setSmallIcon(icon)
                .setContentTitle(notification.title)
                .setContentText(notification.text)
                .setGroup(uniqueKey)
                .setGroupSummary(false)
            val pendingIntent = cache[notification.id]
            if (pendingIntent != null) builder.setContentIntent(pendingIntent)
            notifyManager.notify(notification.id.toInt(), builder.build())
        }
    }

    fun archiveNotification(id: Long) {
        viewModelScope.launch {
            db.archiveNotification(id)
        }
    }
}
