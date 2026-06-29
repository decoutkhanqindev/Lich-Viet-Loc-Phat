package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.MainActivity
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.MainDestination
import com.decoutkhanqindev.lich_viet_loc_phat.utils.navigateTo
import org.koin.compose.koinInject

@Composable
fun SplashScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val activity = context as MainActivity
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adsManager: AdsManager = koinInject()
    val bannerSplashState by adsManager.bannerSplash.state.collectAsStateWithLifecycle()
    val interSplashState by adsManager.interSplash.state.collectAsStateWithLifecycle()
    val handleNavigateToMain= {
        backStack.navigateTo(MainDestination, false)
    }

    LaunchedEffect(Unit) {
        adsManager.bannerSplash.load(context)
    }

    LaunchedEffect(bannerSplashState) {
        if (bannerSplashState == AdUnitState.LOADED) {
            adsManager.interSplash.load(context)
        }
    }

    LaunchedEffect(interSplashState, networkAvailable) {
        when (interSplashState) {
            AdUnitState.LOADED -> {
                adsManager.interSplash.show(
                    activity,
                    onImpression = {
                        if (networkAvailable) handleNavigateToMain()
                    },
                    onAdFailedToShow = {
                        if (networkAvailable) handleNavigateToMain()
                    }
                )
            }

            AdUnitState.IMPRESSION -> adsManager.bannerHome.load(context)

            AdUnitState.FAILED -> handleNavigateToMain()

            else -> Unit
        }
    }

    SplashContent()
}
