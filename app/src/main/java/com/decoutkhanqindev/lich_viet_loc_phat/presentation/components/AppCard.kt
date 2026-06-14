package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.SurfaceCard

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
                shape = RoundedCornerShape12dp
            ),
        shape = RoundedCornerShape12dp,
        color = SurfaceCard,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content = content,
    )
}
