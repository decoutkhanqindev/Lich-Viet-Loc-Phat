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
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmer
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.delay

@Immutable
private enum class AdLoadState { Loading, Loaded, Failed, Impression }

@Composable
fun BannerAd(
    onImpression: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) return

    val context = LocalContext.current

    var adState by remember { mutableStateOf(AdLoadState.Loading) }
    val adSize = remember {
        AdSize.getLargePortraitAnchoredAdaptiveBannerAdSize(context, AdSize.FULL_WIDTH)
    }

    val adView = remember {
        AdView(context).apply {
            setAdSize(adSize)
            adUnitId = BuildConfig.ADMOB_BANNER_ID
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    adState = AdLoadState.Loaded
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    adState = AdLoadState.Failed
                }

                override fun onAdImpression() {
                    adState = AdLoadState.Impression
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    LaunchedEffect(adState) {
        if (adState == AdLoadState.Impression) {
            delay(2000L)
            onImpression()
        }
    }

    LifecycleResumeEffect(adState) {
        if (adState == AdLoadState.Loaded || adState == AdLoadState.Impression) adView.resume()
        onPauseOrDispose {
            if (adState == AdLoadState.Loaded || adState == AdLoadState.Impression) adView.pause()
        }
    }

    if (adState == AdLoadState.Failed) return

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

        if (adState == AdLoadState.Loading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shimmer(isEnable = true)
                    .background(ShimmerBg),
            )
        }
    }
}