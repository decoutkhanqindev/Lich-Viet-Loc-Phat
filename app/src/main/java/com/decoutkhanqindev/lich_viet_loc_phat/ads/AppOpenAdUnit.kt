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
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class AppOpenAdUnit(id: String, name: String) : AdUnit(id, name) {

    private val networkManager: NetworkManager by inject(NetworkManager::class.java)

    private val job = SupervisorJob()
    private val scope =
        CoroutineScope(Dispatchers.Main + job + CoroutineExceptionHandler { _, throwable ->
            Timber.tag(tag).e(throwable.stackTraceToString())
        })

    private var _appOpenAd: AppOpenAd? = null
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
            Timber.tag(tag).d("$name - Loading")
            AppOpenAd.load(
                context,
                id,
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        Timber.tag(tag).d("$name - Loaded")
                        _appOpenAd = ad
                        _state.value = AdUnitState.LOADED
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag(tag).d("$name - Failed: ${error.message}")
                        _state.value = AdUnitState.FAILED
                    }
                },
            )
        }
    }

    fun show(
        activity: Activity,
        onImpression: () -> Unit = {},
        onAdClosed: () -> Unit = {},
        adFailedToShow: () -> Unit = {},
    ) {
        val ad = _appOpenAd ?: return
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdImpression() {
                Timber.tag(tag).d("$name - Impression")
                _state.value = AdUnitState.IMPRESSION
                onImpression()
            }

            override fun onAdDismissedFullScreenContent() {
                Timber.tag(tag).d("$name - Closed")
                _appOpenAd = null
                _state.value = AdUnitState.NONE
                onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Timber.tag(tag).d("$name - Failed to show: ${error.message}")
                _appOpenAd = null
                _state.value = AdUnitState.NONE
                adFailedToShow()
            }
        }
        ad.show(activity)
    }

    override fun destroy() {
        job.cancel()
        _appOpenAd = null
        _state.value = AdUnitState.NONE
    }
}
