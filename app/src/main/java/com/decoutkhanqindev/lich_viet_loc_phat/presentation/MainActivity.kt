package com.decoutkhanqindev.lich_viet_loc_phat.presentation

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.device.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.AppNavDisplay
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.LichVietLocPhatTheme
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    private val networkManager: NetworkManager by inject(NetworkManager::class.java)
    private val adsManager: AdsManager by inject(AdsManager::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            LichVietLocPhatTheme {
                AppNavDisplay(modifier = Modifier.fillMaxSize())
            }
        }
    }

    fun openWifiSettings() {
        try {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showToast(messageRes: Int) {
        Toast.makeText(this, getString(messageRes), Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        networkManager.destroy()
        adsManager.destroyAll()
        super.onDestroy()
    }
}
