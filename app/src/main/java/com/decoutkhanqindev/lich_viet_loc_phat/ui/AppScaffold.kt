package com.decoutkhanqindev.lich_viet_loc_phat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.rememberNavBackStack
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.AppNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.AppBottomNavBar
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.AppTopBar

@Composable
fun AppScaffold() {
    val backStack = rememberNavBackStack(TodayDestination())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = { AppTopBar() },
            bottomBar = { AppBottomNavBar(backStack = backStack) },
        ) { innerPadding ->
            AppNavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}
