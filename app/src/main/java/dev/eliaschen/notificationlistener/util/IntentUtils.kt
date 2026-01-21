package dev.eliaschen.notificationlistener.util

import android.content.Context

fun launchPackage(context: Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    context.startActivity(intent)
}
