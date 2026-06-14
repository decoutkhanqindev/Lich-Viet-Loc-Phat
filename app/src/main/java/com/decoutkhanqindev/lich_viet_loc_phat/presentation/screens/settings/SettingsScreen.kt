package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val viewModel: SettingsViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveOnLifecycleOwner {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingsEffect.ShowMessage ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}

