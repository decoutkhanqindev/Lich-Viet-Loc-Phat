package com.decoutkhanqindev.lich_viet_loc_phat.ads

import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager

class AdsManager(networkManager: NetworkManager) {
    val bannerSplash = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_SPLASH_ID,
        name = "banner_splash",
        networkManager = networkManager,
    )
    val bannerHome = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_HOME_ID,
        name = "banner_home",
        networkManager = networkManager,
    )
    val nativeToday = NativeAdUnit(
        id = BuildConfig.ADMOB_NATIVE_TODAY_ID,
        name = "native_today",
        networkManager = networkManager,
    )
    val nativeCalendar = NativeAdUnit(
        id = BuildConfig.ADMOB_NATIVE_CALENDAR_ID,
        name = "native_calendar",
        networkManager = networkManager,
    )
    val interHome = InterstitialAdUnit(
        id = BuildConfig.ADMOB_INTER_HOME_ID,
        name = "inter_home",
        networkManager = networkManager,
    )

    fun destroyAll() {
        bannerSplash.destroy()
        bannerHome.destroy()
        nativeToday.destroy()
        nativeCalendar.destroy()
        interHome.destroy()
    }
}
