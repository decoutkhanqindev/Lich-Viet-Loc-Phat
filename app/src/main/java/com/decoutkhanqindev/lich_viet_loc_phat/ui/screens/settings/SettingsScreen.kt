package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}

