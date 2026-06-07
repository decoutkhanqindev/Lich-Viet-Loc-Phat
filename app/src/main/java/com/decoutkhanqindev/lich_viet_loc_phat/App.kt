package com.decoutkhanqindev.lich_viet_loc_phat

import android.app.Application
import com.decoutkhanqindev.lich_viet_loc_phat.di.appModule
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupKoin()
        initAdMob()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private fun initAdMob() {
        scope.launch {
            MobileAds.initialize(this@App) { status ->
                Timber.d("AdMob initialized: ${status.adapterStatusMap}")
            }
        }
    }
}
