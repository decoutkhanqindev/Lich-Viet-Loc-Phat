package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.CalendarDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.SettingsDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.navigateToTab
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongAlpha12
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongLightAlpha50
import com.decoutkhanqindev.lich_viet_loc_phat.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.ads.BannerAd

@Immutable
private data class Tab(
    val destination: NavKey,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
) {
    companion object {
        val default by lazy {
            listOf(
                Tab(TodayDestination(), Icons.Default.Today, R.string.tab_today),
                Tab(CalendarDestination, Icons.Default.CalendarMonth, R.string.tab_calendar),
                Tab(SettingsDestination, Icons.Default.Settings, R.string.tab_settings),
            )
        }
    }
}

@Composable
fun AppBottomNavBar(backStack: NavBackStack<NavKey>) {
    val currentDestination = backStack.lastOrNull()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceCard),
        verticalArrangement = Arrangement.Bottom,
    ) {
        HorizontalDivider(color = BorderWarm, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tab.default.forEach { tab ->
                val selected =
                    currentDestination?.let { it::class == tab.destination::class } ?: false
                NavItem(
                    tab = tab,
                    selected = selected,
                    onClick = {
                        backStack.navigateToTab(tab.destination)
                    },
                )
            }
        }
        BannerAd()
    }
}

@Composable
private fun NavItem(
    tab: Tab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = if (selected) VangDong else NauNhat,
        animationSpec = tween(200),
        label = "NavContentColor",
    )
    val pillBg by animateColorAsState(
        targetValue = if (selected) VangDongAlpha12 else Color.Transparent,
        animationSpec = tween(200),
        label = "NavPillBg",
    )
    val pillBorder by animateColorAsState(
        targetValue = if (selected) VangDongLightAlpha50 else Color.Transparent,
        animationSpec = tween(200),
        label = "NavPillBorder",
    )

    val label = stringResource(tab.labelRes)

    Column(
        modifier = Modifier.onClick(
            shape = RoundedCornerShape12dp,
            ripple = false,
        ) {
            onClick()
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(32.dp)
                .background(color = pillBg, shape = RoundedCornerShape16dp)
                .border(
                    width = 1.dp,
                    color = pillBorder,
                    shape = RoundedCornerShape16dp
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
