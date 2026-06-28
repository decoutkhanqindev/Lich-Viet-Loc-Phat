package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads

import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.ads.NativeAdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.shimmerLoading
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.ShimmerBg
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import org.koin.compose.koinInject

@Composable
fun NativeMedia43Ad(
    adUnit: NativeAdUnit,
    modifier: Modifier = Modifier,
) {
    val preview = LocalInspectionMode.current
    val networkManager: NetworkManager = koinInject()
    val networkAvailable by networkManager.available.collectAsStateWithLifecycle()
    val adState by adUnit.state.collectAsStateWithLifecycle()
    val nativeAd = adUnit.nativeAd

    if (preview) return
    if (!networkAvailable) return
    if (adState == AdUnitState.NONE || adState == AdUnitState.FAILED) return

    Box(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            factory = { context ->
                buildNativeMedia43AdView(context)
            },
            update = { view ->
                if (nativeAd != null) bindNativeMedia43Ad(view, nativeAd)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (adState == AdUnitState.LOADING) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shimmerLoading()
                    .background(ShimmerBg),
            )
        }
    }
}

private fun buildNativeMedia43AdView(context: Context): NativeAdView {
    val dp = { v: Int -> (v * context.resources.displayMetrics.density).toInt() }
    val view = LayoutInflater.from(context)
        .inflate(R.layout.native_media_4_3_ad, null, false) as NativeAdView

    val icon = view.findViewById<ImageView>(R.id.ad_icon)
    icon.clipToOutline = true
    icon.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(v: View, outline: Outline) {
            outline.setRoundRect(0, 0, v.width, v.height, dp(8).toFloat())
        }
    }

    val media = view.findViewById<MediaView>(R.id.ad_media)
    media.clipToOutline = true
    media.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(v: View, outline: Outline) {
            outline.setRoundRect(0, 0, v.width, v.height, dp(8).toFloat())
        }
    }

    view.iconView = icon
    view.headlineView = view.findViewById(R.id.ad_headline)
    view.bodyView = view.findViewById(R.id.ad_body)
    view.mediaView = media
    view.callToActionView = view.findViewById(R.id.ad_cta)

    return view
}

private fun bindNativeMedia43Ad(view: NativeAdView, nativeAd: NativeAd) {
    val icon = nativeAd.icon
    (view.iconView as? ImageView)?.apply {
        visibility = if (icon != null) View.VISIBLE else View.GONE
        if (icon != null) setImageDrawable(icon.drawable)
    }
    (view.headlineView as? TextView)?.text = nativeAd.headline
    (view.bodyView as? TextView)?.apply {
        text = nativeAd.body
        visibility = if (nativeAd.body != null) View.VISIBLE else View.GONE
    }
    view.mediaView?.visibility = run {
        val media = nativeAd.mediaContent
        if (media != null && (media.hasVideoContent() || nativeAd.images.isNotEmpty())) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    (view.callToActionView as? TextView)?.apply {
        text = nativeAd.callToAction
        visibility = if (nativeAd.callToAction != null) View.VISIBLE else View.GONE
    }
    view.setNativeAd(nativeAd)
}
