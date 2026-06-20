package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val context = LocalContext.current
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adsManager: AdsManager = koinInject()
    val bannerSplashState by adsManager.bannerSplash.state.collectAsStateWithLifecycle()
    val bannerHomeState by adsManager.bannerHome.state.collectAsStateWithLifecycle()

    LaunchedEffect(networkAvailable) {
        if (!networkAvailable) return@LaunchedEffect
        if (bannerSplashState == AdUnitState.NONE) adsManager.bannerSplash.load(context)
        if (bannerHomeState == AdUnitState.NONE) adsManager.bannerHome.load(context)
    }

    LaunchedEffect(bannerSplashState, networkAvailable) {
        when (bannerSplashState) {
            AdUnitState.FAILED -> {
                delay(AdUnitState.FAILED_DELAY_DURATION)
                if (networkAvailable) onNavigateToMain()
            }

            AdUnitState.IMPRESSION -> {
                delay(AdUnitState.IMPRESSION_DELAY_DURATION)
                onNavigateToMain()
            }

            else -> Unit
        }
    }

    SplashContent()
}
