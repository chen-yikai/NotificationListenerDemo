package dev.eliaschen.noti.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat

fun createNotificationIcon(context: Context, count: Int): IconCompat {
    val size = (24 * context.resources.displayMetrics.density).toInt()
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)

    val accentColor = context.getColor(android.R.color.system_accent1_500)

    val bgPaint = Paint().apply {
        color = accentColor
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, bgPaint)

    val textPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        textSize = size * 0.65f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val xPos = size / 2f
    val yPos = (size / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f)
    canvas.drawText(count.toString(), xPos, yPos, textPaint)

    return IconCompat.createWithBitmap(bitmap)
}
