package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDenMuted

@Composable
fun PrevNextButtons(
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.ChevronLeft,
            contentDescription = "Trước",
            tint = MucDenMuted,
            modifier = Modifier
                .onClick { onPrev() }
                .size(32.dp),
        )
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "Sau",
            tint = MucDenMuted,
            modifier = Modifier
                .onClick { onNext() }
                .size(32.dp),
        )
    }
}