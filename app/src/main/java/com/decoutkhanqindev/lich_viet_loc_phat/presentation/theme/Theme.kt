package com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GiayDoColorScheme = lightColorScheme(
    primary = VangDong,
    secondary = NauAm,
    tertiary = NgocBich,
    background = GiayDo,
    surface = SurfaceCard,
    onBackground = MucDen,
    onSurface = MucDen,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
)

@Composable
fun LichVietLocPhatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GiayDoColorScheme,
        typography = Typography,
        content = content
    )
}
