package com.decoutkhanqindev.lich_viet_loc_phat

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.decoutkhanqindev.lich_viet_loc_phat.theme.LichVietLocPhatTheme
import com.decoutkhanqindev.lich_viet_loc_phat.ui.AppScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            LichVietLocPhatTheme {
                AppScaffold()
            }
        }
    }
}
