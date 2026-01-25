package dev.eliaschen.noti.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.eliaschen.noti.room.dao.NotificationDao
import dev.eliaschen.noti.room.dao.TaskDao
import dev.eliaschen.noti.room.table.NotificationEntity
import dev.eliaschen.noti.room.table.TaskEntity

@Database(entities = [NotificationEntity::class, TaskEntity::class], version = 14)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun todoDao(): TaskDao
}