package com.decoutkhanqindev.lich_viet_loc_phat.presentation

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.AppNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.LichVietLocPhatTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val adsManager: AdsManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            LichVietLocPhatTheme {
                AppNavDisplay(onOpenWifiSettings = { openWifiSettings() })
            }
        }
    }

    private fun openWifiSettings() {
        runCatching { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
    }

    override fun onDestroy() {
        adsManager.destroyAll()
        super.onDestroy()
    }
}
