package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.CalendarDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.ConverterDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.SettingsDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.navigateToTab

@Immutable
private data class Tab(
    val destination: NavKey,
    val icon: ImageVector,
    val label: String,
) {
    companion object {
        val default by lazy {
            listOf(
                Tab(
                    TodayDestination(),
                    Icons.Default.Today,
                    "Hôm Nay"
                ),
                Tab(
                    CalendarDestination,
                    Icons.Default.CalendarMonth,
                    "Lịch"
                ),
                Tab(
                    ConverterDestination,
                    Icons.Default.SwapHoriz,
                    "Đổi Ngày"
                ),
                Tab(
                    SettingsDestination,
                    Icons.Default.Settings,
                    "Cài Đặt"
                )
            )
        }
    }
}

@Composable
fun GlassBottomNavBar(backStack: NavBackStack<NavKey>) {
    val currentDestination = backStack.lastOrNull()

    NavigationBar {
        Tab.default.forEach { tab ->
            val selected = remember(currentDestination) {
                currentDestination == tab.destination
            }
            NavigationBarItem(
                selected = selected,
                onClick = { backStack.navigateToTab(tab.destination) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
            )
        }
    }
}
