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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class RewardAdUnit(id: String, name: String) : AdUnit(id, name) {

    private val networkManager: NetworkManager by inject(NetworkManager::class.java)

    private val job = SupervisorJob()
    private val scope =
        CoroutineScope(Dispatchers.Main + job + CoroutineExceptionHandler { _, throwable ->
            Timber.tag(tag).e(throwable.stackTraceToString())
        })

    private var _rewardedAd: RewardedAd? = null
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
            RewardedAd.load(
                context,
                id,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Timber.tag(tag).d("$name - Loaded")
                        _rewardedAd = ad
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
        onEarned: () -> Unit = {},
        onAdClosed: () -> Unit = {},
        onAdFailedToShow: () -> Unit = {},
    ) {
        val ad = _rewardedAd ?: return

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdImpression() {
                Timber.tag(tag).d("$name - Impression")
                _state.value = AdUnitState.IMPRESSION
            }

            override fun onAdDismissedFullScreenContent() {
                Timber.tag(tag).d("$name - Closed")
                _rewardedAd = null
                _state.value = AdUnitState.NONE
                onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Timber.tag(tag).d("$name - Failed to show: ${error.message}")
                _rewardedAd = null
                _state.value = AdUnitState.NONE
                onAdFailedToShow()
            }
        }

        ad.show(activity) {
            Timber.tag(tag).d("$name - Reward earned")
            onEarned()
        }
    }

    override fun destroy() {
        job.cancel()
        _rewardedAd = null
        _state.value = AdUnitState.NONE
    }
}
