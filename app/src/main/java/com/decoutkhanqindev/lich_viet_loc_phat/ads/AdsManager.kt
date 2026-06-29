package com.decoutkhanqindev.lich_viet_loc_phat.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads.AdUnitState

class AdsManager(private val application: Application) :
    DefaultLifecycleObserver, Application.ActivityLifecycleCallbacks {

    val bannerSplash = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_SPLASH_ID,
        name = "banner_splash",
    )
    val bannerHome = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_HOME_ID,
        name = "banner_home",
    )
    val nativeToday = NativeAdUnit(
        id = BuildConfig.ADMOB_NATIVE_TODAY_ID,
        name = "native_today",
    )
    val nativeCalendar = NativeAdUnit(
        id = BuildConfig.ADMOB_NATIVE_CALENDAR_ID,
        name = "native_calendar",
    )
    val interHome = InterstitialAdUnit(
        id = BuildConfig.ADMOB_INTER_HOME_ID,
        name = "inter_home",
        onShowing = { isShowingAd = true },
        onClosed = { isShowingAd = false },
        onFailedToShow = { isShowingAd = false }
    )
    val interSplash = InterstitialAdUnit(
        id = BuildConfig.ADMOB_INTER_SPLASH_ID,
        name = "inter_splash",
        onShowing = { isShowingAd = true },
        onClosed = { isShowingAd = false },
        onFailedToShow = { isShowingAd = false }
    )
    val rewardWidget = RewardAdUnit(
        id = BuildConfig.ADMOB_REWARD_WIDGET_ID,
        name = "reward_widget",
        onShowing = { isShowingAd = true },
        onClosed = { isShowingAd = false },
        onFailedToShow = { isShowingAd = false }
    )
    val appOpenResume = AppOpenAdUnit(
        id = BuildConfig.ADMOB_APP_OPEN_RESUME_ID,
        name = "app_open_resume",
    )

    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var isDisableAppOpenResumeAtTime = false

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) {
        if (currentActivity == activity) currentActivity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onStart(owner: LifecycleOwner) {
        val activity = currentActivity ?: return

        if (isShowingAd) return

        if (isDisableAppOpenResumeAtTime) {
            isDisableAppOpenResumeAtTime = false
            return
        }

        if (appOpenResume.state.value == AdUnitState.LOADED) {
            appOpenResume.show(activity)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        appOpenResume.load(application)
    }

    fun disableAppOpenResumeAtTime() {
        isDisableAppOpenResumeAtTime = true
    }

    fun destroyAll() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        application.unregisterActivityLifecycleCallbacks(this)
        bannerSplash.destroy()
        bannerHome.destroy()
        nativeToday.destroy()
        nativeCalendar.destroy()
        interHome.destroy()
        rewardWidget.destroy()
        appOpenResume.destroy()
    }
}
