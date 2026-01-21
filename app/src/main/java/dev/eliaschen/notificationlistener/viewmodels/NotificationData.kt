package dev.eliaschen.notificationlistener.viewmodels

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
class NotificationViewModel @Inject constructor(private val db: NotificationDao) : ViewModel() {
    val notifications: StateFlow<List<NotificationEntity>> = db.getAllNotifications().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            db.addNotification(notification)
        }
    }

    fun deleteNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            db.deleteNotification(notification)
        }
    }
}