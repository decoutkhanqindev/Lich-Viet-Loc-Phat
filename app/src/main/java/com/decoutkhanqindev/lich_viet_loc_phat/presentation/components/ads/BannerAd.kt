package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmer
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.delay
import timber.log.Timber

@Immutable
private enum class AdUnitState { Loading, Loaded, Failed, Impression }

@Immutable
data class BannerAdUnit(
    val id: String,
    val networkAvailable: Boolean = true,
    val onLoadFailed: () -> Unit = {},
    val onImpression: () -> Unit = {},
)

@Composable
fun BannerAd(
    adUnit: BannerAdUnit,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) return
    if (!adUnit.networkAvailable) return

    val context = LocalContext.current
    var adState by remember { mutableStateOf(AdUnitState.Loading) }
    val adSize = remember {
        AdSize.getLargePortraitAnchoredAdaptiveBannerAdSize(context, AdSize.FULL_WIDTH)
    }
    val adRequest = remember { AdRequest.Builder().build() }

    val adView = remember {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = adUnit.id
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Timber.tag("AdUnit").d("BannerAd - $adUnitId - Loaded")
                    adState = AdUnitState.Loaded
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Timber.tag("AdUnit").d("BannerAd - $adUnitId - Failed: ${error.message}")
                    adState = AdUnitState.Failed
                }

                override fun onAdImpression() {
                    Timber.tag("AdUnit").d("BannerAd - $adUnitId - Impression")
                    adState = AdUnitState.Impression
                }
            }
            loadAd(adRequest)
        }
    }

    LaunchedEffect(adState) {
        when (adState) {
            AdUnitState.Loading -> Timber.tag("AdUnit").d("BannerAd - ${adView.adUnitId} - Loading")
            AdUnitState.Failed -> {
                delay(1500L)
                adUnit.onLoadFailed()
            }
            AdUnitState.Impression -> {
                delay(2500L)
                adUnit.onImpression()
            }

            else -> Unit
        }
    }

    LifecycleResumeEffect(adState) {
        if (adState == AdUnitState.Loaded || adState == AdUnitState.Impression) adView.resume()
        onPauseOrDispose {
            if (adState == AdUnitState.Loaded || adState == AdUnitState.Impression) adView.pause()
        }
    }

    if (adState == AdUnitState.Failed) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(adSize.height.dp),
    ) {
        AndroidView(
            factory = { adView },
            modifier = Modifier.fillMaxWidth(),
            onRelease = { view -> view.destroy() }
        )

        if (adState == AdUnitState.Loading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shimmer(isEnable = true)
                    .background(ShimmerBg),
            )
        }
    }
}