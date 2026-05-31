package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen() {
    val activity = LocalContext.current as ComponentActivity
    val viewModel: SettingsViewModel = koinViewModel(viewModelStoreOwner = activity)
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}

