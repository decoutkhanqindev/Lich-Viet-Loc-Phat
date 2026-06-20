package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.content.Context
import android.util.DisplayMetrics
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import timber.log.Timber

class BannerAdUnit(id: String, name: String) : AdUnit(id, name) {
    private var _adView: AdView? = null
    val adView: AdView? get() = _adView

    override fun load(context: Context) {
        if (_adView == null) {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            val screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)

            _adView = AdView(context).also { view ->
                view.adUnitId = id
                view.setAdSize(adSize)
                view.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Timber.tag("BannerAdUnit").d("$name - Loaded")
                        _state.value = AdUnitState.LOADED
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag("BannerAdUnit").d("$name - Failed: ${error.message}")
                        _state.value = AdUnitState.FAILED
                    }

                    override fun onAdImpression() {
                        Timber.tag("BannerAdUnit").d("$name - Impression")
                        _state.value = AdUnitState.IMPRESSION
                    }
                }
            }
        }

        _state.value = AdUnitState.LOADING
        Timber.tag("BannerAdUnit").d("$name - Loading")

        _adView?.loadAd(AdRequest.Builder().build())
    }

    override fun pause() {
        _adView?.pause()
    }

    override fun resume() {
        _adView?.resume()
    }

    override fun destroy() {
        _adView?.destroy()
        _adView = null
        _state.value = AdUnitState.NONE
    }
}