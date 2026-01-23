package dev.eliaschen.notificationlistener.ui.theme

import android.os.Build.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.eliaschen.notificationlistener.R

val notoSerif = FontFamily(
    Font(resId = R.font.noto_serif, weight = FontWeight.Light),
    Font(resId = R.font.noto_serif, weight = FontWeight.Normal),
    Font(resId = R.font.noto_serif, weight = FontWeight.Bold),
)
val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = notoSerif),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = notoSerif),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = notoSerif),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = notoSerif),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = notoSerif),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = notoSerif),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = notoSerif),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = notoSerif),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = notoSerif),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = notoSerif),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = notoSerif),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = notoSerif),
)

@Composable
fun NotificationListenerDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        VERSION.SDK_INT >= VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
//        typography = Typography,
        content = content
    )
}