package dev.eliaschen.noti.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.eliaschen.noti.room.table.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notification WHERE archived = 1")
    fun getArchivedNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notification WHERE archived = 0")
    fun getActiveNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notification WHERE packageName = :packageName AND title = :title AND text = :text AND notificationId = :notificationId AND archived = 0")
    suspend fun isDuplicateNotification(packageName: String, title: String, text: String, notificationId: Int): Int

    @Insert
    suspend fun addNotification(notification: NotificationEntity): Long

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("UPDATE notification SET archived = 1 WHERE id = :id")
    suspend fun archiveNotification(id: Long)
}