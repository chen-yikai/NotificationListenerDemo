package dev.eliaschen.noti.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.eliaschen.noti.room.dao.NotificationDao
import dev.eliaschen.noti.room.dao.TodoDao
import dev.eliaschen.noti.room.table.NotificationEntity
import dev.eliaschen.noti.room.table.TodoEntity

@Database(entities = [NotificationEntity::class, TodoEntity::class], version = 11)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun todoDao(): TodoDao
}