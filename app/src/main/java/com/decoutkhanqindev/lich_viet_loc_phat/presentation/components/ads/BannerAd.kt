package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmer
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Immutable
private enum class AdLoadState { Loading, Loaded, Failed }

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    if (LocalInspectionMode.current) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_RESUME -> adView.resume()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
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
