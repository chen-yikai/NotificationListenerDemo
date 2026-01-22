package dev.eliaschen.notificationlistener

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp

const val notification_channel = "notification_label_channel"

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    private fun createNotificationChannel() {
        
    }
}