package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

import android.app.Application
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.device.SharedPrefsManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidget
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidgetReceiver
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val context: Application,
    private val sharedPrefs: SharedPrefsManager,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(
        showCanChiOnCell = sharedPrefs.showCanChiOnCell,
        calendarWidgetEnabled = sharedPrefs.calendarWidgetEnabled,
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

            is SettingsIntent.ToggleCalendarWidget -> {
                updateState { copy(calendarWidgetEnabled = intent.enabled) }
                if (intent.enabled) pinWidget()
            }
        }
    }

    private fun pinWidget() {
        viewModelScope.launch {
            runCatching {
                GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
                    receiver = CalendarWidgetReceiver::class.java,
                    preview = CalendarWidget(),
                )
            }.onSuccess {
                sharedPrefs.calendarWidgetEnabled = true
                sendEffect(SettingsEffect.ShowMessage("Thêm widget thành công!"))
            }.onFailure {
                sharedPrefs.calendarWidgetEnabled = false
                updateState { copy(calendarWidgetEnabled = false) }
                sendEffect(SettingsEffect.ShowMessage("Không thể thêm widget. Vui lòng thử lại."))
            }
        }
    }

    private fun refreshWidget() {
        viewModelScope.launch {
            runCatching { CalendarWidget().updateAll(context) }
        }
    }
}
