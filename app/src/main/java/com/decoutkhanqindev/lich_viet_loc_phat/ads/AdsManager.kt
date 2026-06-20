package com.decoutkhanqindev.lich_viet_loc_phat.ads

import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig

class AdsManager {
    val bannerSplash = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_SPLASH_ID,
        name = "banner_splash"
    )
    val bannerHome = BannerAdUnit(
        id = BuildConfig.ADMOB_BANNER_HOME_ID,
        name = "banner_home"
    )
}
