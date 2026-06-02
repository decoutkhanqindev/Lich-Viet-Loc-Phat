package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderWarm, shape = shape),
        shape = shape,
        color = SurfaceCard,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content = content,
    )
}
