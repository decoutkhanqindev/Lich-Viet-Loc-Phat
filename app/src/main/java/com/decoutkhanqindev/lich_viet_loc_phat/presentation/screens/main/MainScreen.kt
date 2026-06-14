package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppBottomNavBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppTopBar
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.NoInternetDialog
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.AppNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.TodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state.MainEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state.MainIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun MainScreen(onOpenNetworkSettings: () -> Unit) {
    val viewModel: MainViewModel = koinActivityViewModel()
    val backStack = rememberNavBackStack(TodayDestination())
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveOnLifecycleOwner {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                MainEffect.OpenNetworkSettings -> onOpenNetworkSettings()
            }
        }
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
                    backStack = backStack
                )
            },
        ) { innerPadding ->
            AppNavDisplay(
                backStack = backStack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }

        if (!state.isOnline) {
            NoInternetDialog(
                onOpenSettings = {
                    viewModel.onIntent(MainIntent.OpenNetworkSettings)
                },
            )
        }
    }
}
