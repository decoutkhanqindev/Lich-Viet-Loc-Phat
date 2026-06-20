package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.ads.BannerAdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmer
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import org.koin.compose.koinInject

@Composable
fun BannerAd(
    adUnit: BannerAdUnit,
    modifier: Modifier = Modifier,
) {
    val preview = LocalInspectionMode.current
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adState by adUnit.state.collectAsStateWithLifecycle()
    val adView = adUnit.adView

    if (preview) return
    if (!networkAvailable) return
    if (adState == AdUnitState.NONE || adState == AdUnitState.FAILED) return
    if (adView == null) return

    val adHeight = adView.adSize?.height ?: 50

    LifecycleResumeEffect(Unit) {
        if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) adUnit.resume()
        onPauseOrDispose {
            if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) adUnit.pause()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(adHeight.dp),
    ) {
        AndroidView(
            factory = { adView },
            modifier = Modifier.fillMaxWidth(),
        )

        if (adState == AdUnitState.LOADING) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shimmer(isEnable = true)
                    .background(ShimmerBg),
            )
        }
    }
}
