package com.decoutkhanqindev.lich_viet_loc_phat.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.AppNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.GlassBottomNavBar
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.GlassTopAppBar

@Composable
fun AppScaffold() {
    val backStack = rememberNavBackStack(TodayDestination())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { GlassTopAppBar() },
        bottomBar = { GlassBottomNavBar(backStack = backStack) },
    ) { innerPadding ->
        AppNavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}
