package com.decoutkhanqindev.lich_viet_loc_phat.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val ClassicDarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    secondary = GoldLight,
    tertiary = IvoryWhite,
    background = NauToi,
    surface = BaTrauDark,
)

private val ClassicLightColorScheme = lightColorScheme(
    primary = GoldAccent,
    secondary = BaTrauMid,
    tertiary = BaTrauDark,
)

@Composable
fun LichVietLocPhatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> ClassicDarkColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
