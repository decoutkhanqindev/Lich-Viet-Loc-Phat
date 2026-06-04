package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong

@Composable
fun TodayButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(150)),
        ) {
            Icon(
                Icons.Default.Today,
                contentDescription = "Hôm nay",
                tint = VangDong,
                modifier = Modifier
                    .onClick { onClick() }
                    .size(28.dp),
            )
        }
    }
}