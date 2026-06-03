package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsState
import com.decoutkhanqindev.lich_viet_loc_phat.widget.CalendarWidget
import com.decoutkhanqindev.lich_viet_loc_phat.widget.CalendarWidgetReceiver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val prefs: SharedPreferences,
    private val context: Application,
) : ViewModel() {

    companion object {
        private const val KEY_SHOW_CAN_CHI_ON_CELL = "show_can_chi_on_cell"
        private const val KEY_CALENDAR_WIDGET_ENABLED = "calendar_widget_enabled"
    }

    private val _state = MutableStateFlow(
        SettingsState(
            showCanChiOnCell = prefs.getBoolean(KEY_SHOW_CAN_CHI_ON_CELL, false),
            calendarWidgetEnabled = prefs.getBoolean(KEY_CALENDAR_WIDGET_ENABLED, false),
            appVersion = BuildConfig.VERSION_NAME,
        )
    )
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _effect = Channel<SettingsEffect>(Channel.BUFFERED)
    val effect: Flow<SettingsEffect> = _effect.receiveAsFlow()

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleCanChiOnCell -> {
                _state.update { it.copy(showCanChiOnCell = intent.enabled) }
                prefs.edit { putBoolean(KEY_SHOW_CAN_CHI_ON_CELL, intent.enabled) }
                refreshWidget()
            }

            is SettingsIntent.ToggleCalendarWidget -> {
                _state.update { it.copy(calendarWidgetEnabled = intent.enabled) }
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
                prefs.edit { putBoolean(KEY_CALENDAR_WIDGET_ENABLED, true) }
                _effect.send(SettingsEffect.ShowMessage("Thêm widget thành công!"))
            }.onFailure {
                prefs.edit { putBoolean(KEY_CALENDAR_WIDGET_ENABLED, false) }
                _effect.send(SettingsEffect.ShowMessage("Không thể thêm widget. Vui lòng thử lại."))
            }
        }
    }

    private fun refreshWidget() {
        viewModelScope.launch {
            runCatching { CalendarWidget().updateAll(context) }
        }
    }
}
