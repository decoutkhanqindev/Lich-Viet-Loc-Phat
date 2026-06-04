package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape12dp

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderWarm,
                shape = roundedCornerShape12dp
            ),
        shape = roundedCornerShape12dp,
        color = SurfaceCard,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content = content,
    )
}
