package dev.eliaschen.notificationlistener

import android.app.PendingIntent
import androidx.collection.LruCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PendingIntentCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 16

    @Provides
    @Singleton
    fun provideLruCache(): LruCache<Long, PendingIntent> {
        return LruCache(cacheSize)
    }
}
