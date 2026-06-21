package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.content.Context
import android.util.DisplayMetrics
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class BannerAdUnit(
    id: String,
    name: String,
    private val networkManager: NetworkManager,
) : AdUnit(id, name) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var _adView: AdView? = null
    val adView: AdView? get() = _adView

    private var networkObserveJob: Job? = null

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
            if (_adView == null) {
                val displayMetrics: DisplayMetrics = context.resources.displayMetrics
                val screenWidthDp = (displayMetrics.widthPixels / displayMetrics.density).toInt()
                val adSize =
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidthDp)

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
    }

    fun pause() {
        _adView?.pause()
    }

    fun resume() {
        _adView?.resume()
    }

    override fun destroy() {
        job.cancel()
        _adView?.destroy()
        _adView = null
        _state.value = AdUnitState.NONE
    }
}
