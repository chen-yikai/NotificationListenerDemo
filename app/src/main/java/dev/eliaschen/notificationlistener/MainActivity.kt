package dev.eliaschen.notificationlistener

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.eliaschen.notificationlistener.ui.NotificationList
import dev.eliaschen.notificationlistener.ui.theme.NotificationListenerDemoTheme

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
            NotificationListenerDemoTheme {
                val context = LocalContext.current
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    if (it) Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
                val hasNotificationPermission =
                    context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                LaunchedEffect(Unit) {
                    if (!isNotificationServiceEnabled()) {
                        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    }
                    if (!hasNotificationPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                NotificationList()
            }
        }
    }
}