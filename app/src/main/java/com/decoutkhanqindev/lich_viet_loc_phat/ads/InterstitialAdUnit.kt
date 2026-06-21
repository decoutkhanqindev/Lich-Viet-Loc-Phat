package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.app.Activity
import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class InterstitialAdUnit(
    id: String,
    name: String,
    private val networkManager: NetworkManager,
) : AdUnit(id, name) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var _interstitialAd: InterstitialAd? = null
    private var currentTabCount = 0
    private var lastShowTime = 0L
    private var networkObserveJob: Job? = null

    fun incrementTabCount() {
        currentTabCount++
    }

    fun readyToLoad(): Boolean {
        val now = System.currentTimeMillis()
        val intervalPassed = lastShowTime == 0L || now - lastShowTime >= INTERVAL
        return currentTabCount >= TAB_THRESHOLD && intervalPassed
    }

    override fun load(context: Context) {
        if (_state.value != AdUnitState.NONE) return

        if (!networkManager.available.value) {
            if (networkObserveJob?.isActive != true) {
                networkObserveJob = scope.launch {
                    networkManager.available.collect { available ->
                        if (available) load(context)
                    }
                }
            }
            return
        }

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
                lastShowTime = System.currentTimeMillis()
                currentTabCount = 0
                _state.value = AdUnitState.IMPRESSION
            }

            override fun onAdDismissedFullScreenContent() {
                Timber.tag("InterstitialAdUnit").d("$name - Closed")
                _interstitialAd = null
                _state.value = AdUnitState.NONE
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Timber.tag("InterstitialAdUnit").d("$name - Failed to show: ${error.message}")
                _interstitialAd = null
                _state.value = AdUnitState.NONE
            }
        }
        ad.show(activity)
    }

    override fun destroy() {
        job.cancel()
        _interstitialAd = null
        _state.value = AdUnitState.NONE
    }

    companion object {
        private const val TAB_THRESHOLD = 3
        private const val INTERVAL = 60_000L
    }
}
