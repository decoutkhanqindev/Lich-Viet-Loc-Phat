package com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.NoInternetDialog
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.MainScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import org.koin.compose.koinInject

@Composable
fun AppNavDisplay(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(SplashDestination)
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()

    if (!networkAvailable) NoInternetDialog()

    NavDisplay(
        entries = rememberDecoratedNavEntries(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<SplashDestination> { SplashScreen(backStack) }
                entry<MainDestination> { MainScreen() }
            },
        ),
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
    )
}
