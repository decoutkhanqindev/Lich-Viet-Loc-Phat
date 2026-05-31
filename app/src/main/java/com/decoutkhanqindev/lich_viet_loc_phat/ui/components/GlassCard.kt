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
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassTint

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit,
) {
    val shape = remember { RoundedCornerShape(cornerRadius) }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = GlassBorder,
                shape = shape
            ),
        shape = shape,
        color = GlassTint,
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        content = content,
    )
}
