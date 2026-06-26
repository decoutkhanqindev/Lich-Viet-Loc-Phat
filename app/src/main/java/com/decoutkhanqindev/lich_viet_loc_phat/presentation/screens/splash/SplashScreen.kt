package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import org.koin.compose.koinInject

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adsManager: AdsManager = koinInject()
    val bannerSplashState by adsManager.bannerSplash.state.collectAsStateWithLifecycle()
    val interSplashState by adsManager.interSplash.state.collectAsStateWithLifecycle()

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
                        if (networkAvailable) onNavigateToMain()
                    },
                    onAdFailedToShow = {
                        if (networkAvailable) onNavigateToMain()
                    }
                )
            }

            AdUnitState.IMPRESSION -> {
                adsManager.bannerHome.load(context)
            }

            AdUnitState.FAILED -> {
                onNavigateToMain()
            }

            else -> Unit
        }
    }

    SplashContent()
}
