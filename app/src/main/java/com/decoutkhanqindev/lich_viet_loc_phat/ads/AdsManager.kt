package com.decoutkhanqindev.lich_viet_loc_phat.ads

import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager

class AdsManager(networkManager: NetworkManager) {
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
    )
    val interSplash = InterstitialAdUnit(
        id = BuildConfig.ADMOB_INTER_SPLASH_ID,
        name = "inter_splash",
    )
    val rewardWidget = RewardAdUnit(
        id = BuildConfig.ADMOB_REWARD_WIDGET_ID,
        name = "reward_widget",
    )

    fun destroyAll() {
        bannerSplash.destroy()
        bannerHome.destroy()
        nativeToday.destroy()
        nativeCalendar.destroy()
        interHome.destroy()
        rewardWidget.destroy()
    }
}
