package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppBottomNavBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppTopBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.MainNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import org.koin.compose.koinInject

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(TodayDestination())
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adsManager: AdsManager = koinInject()
    val nativeTodayState by adsManager.nativeToday.state.collectAsStateWithLifecycle()
    val nativeCalendarState by adsManager.nativeCalendar.state.collectAsStateWithLifecycle()

    LaunchedEffect(networkAvailable) {
        if (!networkAvailable) return@LaunchedEffect
        if (nativeTodayState == AdUnitState.NONE) adsManager.nativeToday.load(context)
        if (nativeCalendarState == AdUnitState.NONE) adsManager.nativeCalendar.load(context)
    }

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
