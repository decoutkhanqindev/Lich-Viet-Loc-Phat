package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}

