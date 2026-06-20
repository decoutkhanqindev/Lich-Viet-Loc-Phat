package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmer
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import org.koin.compose.koinInject

@Composable
fun BannerAd(
    adUnit: BannerAdUnit,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) return

    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    if (!networkAvailable) return

    val adState by adUnit.state.collectAsStateWithLifecycle()
    if (adState == AdUnitState.NONE) return
    val adView = adUnit.adView ?: return

    LifecycleResumeEffect(Unit) {
        if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) adUnit.resume()
        onPauseOrDispose {
            if (adState == AdUnitState.LOADED || adState == AdUnitState.IMPRESSION) adUnit.pause()
        }
    }

    if (adState == AdUnitState.FAILED) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn((adView.adSize?.height ?: 50).dp),
    ) {
        AndroidView(
            factory = { adView },
            modifier = Modifier.fillMaxWidth(),
            onRelease = { adUnit.destroy() },
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