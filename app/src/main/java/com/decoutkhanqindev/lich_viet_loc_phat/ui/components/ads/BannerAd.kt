package com.decoutkhanqindev.lich_viet_loc_phat.ui.components.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd() {
    val preview = LocalInspectionMode.current
    if (preview) return

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val adView = remember {
        AdView(context).apply {
            setAdSize(
                AdSize.getLargePortraitAnchoredAdaptiveBannerAdSize(
                    context,
                    AdSize.FULL_WIDTH
                )
            )
            adUnitId = BuildConfig.ADMOB_BANNER_ID
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

    AndroidView(
        factory = { adView },
        modifier = Modifier.fillMaxWidth()
    )
}
