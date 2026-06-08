package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = VangDong,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.Transparent,
            titleContentColor = Color.Transparent,
            actionIconContentColor = Color.Transparent,
        ),
    )
}
