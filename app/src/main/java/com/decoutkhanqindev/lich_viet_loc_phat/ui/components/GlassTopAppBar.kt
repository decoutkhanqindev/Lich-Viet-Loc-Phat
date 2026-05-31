package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BaTrauDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Lịch Việt Lộc Phát",
                color = GoldAccent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BaTrauDark,
            scrolledContainerColor = BaTrauDark,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),
    )
}
