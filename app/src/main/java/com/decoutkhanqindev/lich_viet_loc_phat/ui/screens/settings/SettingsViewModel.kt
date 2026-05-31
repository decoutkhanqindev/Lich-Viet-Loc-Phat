package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val prefs: SharedPreferences,
) : ViewModel() {

    companion object {
        private const val KEY_SHOW_CAN_CHI_ON_CELL = "show_can_chi_on_cell"
    }

    private val _state = MutableStateFlow(
        SettingsState(
            showCanChiOnCell = prefs.getBoolean(KEY_SHOW_CAN_CHI_ON_CELL, true),
            appVersion = BuildConfig.VERSION_NAME,
        )
    )
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleCanChiOnCell -> {
                _state.update { it.copy(showCanChiOnCell = intent.enabled) }
                prefs.edit { putBoolean(KEY_SHOW_CAN_CHI_ON_CELL, intent.enabled) }
            }
        }
    }
}
