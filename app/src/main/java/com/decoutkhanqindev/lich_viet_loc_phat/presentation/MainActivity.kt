package com.decoutkhanqindev.lich_viet_loc_phat.presentation

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.MainScreen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.LichVietLocPhatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            LichVietLocPhatTheme {
                MainScreen(onOpenNetworkSettings = { openNetworkSettings() })
            }
        }
    }

    private fun openNetworkSettings() {
        runCatching { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
    }
}
