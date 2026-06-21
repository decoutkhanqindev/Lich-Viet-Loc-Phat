package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class NativeAdUnit(
    id: String,
    name: String,
    private val networkManager: NetworkManager,
) : AdUnit(id, name) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private var _nativeAd: NativeAd? = null
    val nativeAd: NativeAd? get() = _nativeAd

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
    }

    override fun destroy() {
        job.cancel()
        _nativeAd?.destroy()
        _nativeAd = null
        _state.value = AdUnitState.NONE
    }
}
