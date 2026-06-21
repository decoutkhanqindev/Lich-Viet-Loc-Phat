package com.decoutkhanqindev.lich_viet_loc_phat

import android.app.Application
import com.decoutkhanqindev.lich_viet_loc_phat.di.appModule
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

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
        // remove this in production
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("5B9A6B9AE0D81DDFBC4AA92424953A94"))
                .build()
        )
        scope.launch {
            MobileAds.initialize(this@App) { status ->
                Timber.d("AdMob initialized: ${status.adapterStatusMap}")
            }
        }
    }

    override fun onTerminate() {
        job.cancel()
        super.onTerminate()
    }
}
