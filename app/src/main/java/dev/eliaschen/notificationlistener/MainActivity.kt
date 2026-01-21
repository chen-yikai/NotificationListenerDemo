package dev.eliaschen.notificationlistener

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.eliaschen.notificationlistener.ui.NotificationList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private fun isNotificationServiceEnabled(): Boolean {
        return NotificationManagerCompat
            .getEnabledListenerPackages(this)
            .contains(packageName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    if (!isNotificationServiceEnabled()) {
                        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    }
                }
                NotificationList()
        }
    }
}