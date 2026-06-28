package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

import android.app.Application
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.device.SharedPrefsManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidget
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val context: Application,
    private val sharedPrefs: SharedPrefsManager,
) : BaseViewModel<SettingsState, SettingsIntent, Nothing>(
    initialState = SettingsState(
        showCanChiOnCell = sharedPrefs.showCanChiOnCell,
        appVersion = BuildConfig.VERSION_NAME,
    )
) {

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleCanChiOnCell -> {
                updateState { copy(showCanChiOnCell = intent.enabled) }
                sharedPrefs.showCanChiOnCell = intent.enabled
                refreshWidget()
            }
        }
    }

    private fun refreshWidget() {
        viewModelScope.launch {
            runCatching { CalendarWidget().updateAll(context) }
        }
    }
}
