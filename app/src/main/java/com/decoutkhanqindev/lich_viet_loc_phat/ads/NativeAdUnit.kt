package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import timber.log.Timber

class NativeAdUnit(id: String, name: String) : AdUnit(id, name) {
    private var _nativeAd: NativeAd? = null
    val nativeAd: NativeAd? get() = _nativeAd

    override fun load(context: Context) {
        _state.value = AdUnitState.LOADING
        Timber.tag("NativeAdUnit").d("$name - Loading")
        AdLoader.Builder(context, id)
            .forNativeAd { ad ->
                _nativeAd?.destroy()
                _nativeAd = ad
                _state.value = AdUnitState.LOADED
                Timber.tag("NativeAdUnit").d("$name - Loaded")
            }
            .withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag("NativeAdUnit").d("$name - Failed: ${error.message}")
                        _state.value = AdUnitState.FAILED
                    }

                    override fun onAdImpression() {
                        Timber.tag("NativeAdUnit").d("$name - Impression")
                        _state.value = AdUnitState.IMPRESSION
                    }
                }
            )
            .build()
            .loadAd(AdRequest.Builder().build())
    }

    override fun pause() = Unit
    override fun resume() = Unit

    override fun destroy() {
        _nativeAd?.destroy()
        _nativeAd = null
        _state.value = AdUnitState.NONE
    }
}
