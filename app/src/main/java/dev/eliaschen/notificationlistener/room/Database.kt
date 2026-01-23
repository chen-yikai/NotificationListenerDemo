package dev.eliaschen.notificationlistener.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.eliaschen.notificationlistener.room.dao.NotificationDao
import dev.eliaschen.notificationlistener.room.dao.TodoDao
import dev.eliaschen.notificationlistener.room.table.NotificationEntity
import dev.eliaschen.notificationlistener.room.table.TodoEntity

@Database(entities = [NotificationEntity::class, TodoEntity::class], version = 9)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun todoDao(): TodoDao
}