package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.app.Activity
import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class InterstitialAdUnit(id: String, name: String) : AdUnit(id, name) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var _interstitialAd: InterstitialAd? = null

    override fun load(context: Context) {
        if (_state.value == AdUnitState.LOADING || _state.value == AdUnitState.LOADED) return
        scope.launch {
            _state.value = AdUnitState.LOADING
            Timber.tag("InterstitialAdUnit").d("$name - Loading")
            InterstitialAd.load(
                context,
                id,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Timber.tag("InterstitialAdUnit").d("$name - Loaded")
                        _interstitialAd = ad
                        _state.value = AdUnitState.LOADED
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag("InterstitialAdUnit").d("$name - Failed: ${error.message}")
                        _state.value = AdUnitState.FAILED
                    }
                },
            )
        }
    }

    fun show(activity: Activity) {
        val ad = _interstitialAd ?: return
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdImpression() {
                Timber.tag("InterstitialAdUnit").d("$name - Impression")
                _state.value = AdUnitState.IMPRESSION
            }

            override fun onAdDismissedFullScreenContent() {
                Timber.tag("InterstitialAdUnit").d("$name - Dismissed")
                _interstitialAd = null
                _state.value = AdUnitState.NONE
                load(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Timber.tag("InterstitialAdUnit").d("$name - Failed to show: ${error.message}")
                _interstitialAd = null
                _state.value = AdUnitState.NONE
            }
        }
        ad.show(activity)
    }

    override fun pause() = Unit
    override fun resume() = Unit

    override fun destroy() {
        job.cancel()
        _interstitialAd = null
        _state.value = AdUnitState.NONE
    }
}
