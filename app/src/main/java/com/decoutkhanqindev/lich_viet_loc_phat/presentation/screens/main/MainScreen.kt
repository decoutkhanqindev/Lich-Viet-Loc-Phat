package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.rememberNavBackStack
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppBottomNavBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppTopBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.MainNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo

@Composable
fun MainScreen() {
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
            bottomBar = {
                AppBottomNavBar(
                    currentDestination = backStack.lastOrNull(),
                    onNavigateTo = backStack::navigateTo,
                )
            },
        ) { innerPadding ->
            MainNavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    }
}
