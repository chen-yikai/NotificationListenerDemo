package dev.eliaschen.notificationlistener.viewmodel

import android.app.PendingIntent
import androidx.collection.LruCache
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val cache: LruCache<Long, PendingIntent>
) : ViewModel() {
    val notifications: StateFlow<List<NotificationEntity>> = db.getAllNotifications().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            db.deleteNotification(notification)
        }
    }
}