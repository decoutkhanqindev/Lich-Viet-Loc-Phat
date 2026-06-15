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
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.common.NetworkViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.NoInternetDialog
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.MainScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavDisplay(
    onOpenWifiSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: NetworkViewModel = koinViewModel()
    val networkAvailable by viewModel.available.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(SplashDestination)

    if (!networkAvailable) NoInternetDialog(onOpenWifiSettings)

    NavDisplay(
        entries = rememberDecoratedNavEntries(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<SplashDestination> {
                    SplashScreen(
                        networkAvailable = networkAvailable,
                        onAdLoadFailed = {
                            if (networkAvailable) backStack.navigateTo(MainDestination, false)
                        },
                        onAdImpression = {
                            backStack.navigateTo(MainDestination, false)
                        },
                    )
                }
                entry<MainDestination> { MainScreen(networkAvailable) }
            },
        ),
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
    )
}
