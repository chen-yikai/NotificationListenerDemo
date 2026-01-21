package dev.eliaschen.notificationlistener.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert
    suspend fun addNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}