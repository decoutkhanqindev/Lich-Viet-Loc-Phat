package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

import android.app.Application
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.BuildConfig
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetCalendarWidgetEnabledUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetShowCanChiOnCellUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.SetCalendarWidgetEnabledUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.SetShowCanChiOnCellUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidget
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidgetReceiver
import kotlinx.coroutines.launch

class SettingsViewModel(
    getShowCanChiOnCell: GetShowCanChiOnCellUseCase,
    getCalendarWidgetEnabled: GetCalendarWidgetEnabledUseCase,
    private val setShowCanChiOnCell: SetShowCanChiOnCellUseCase,
    private val setCalendarWidgetEnabled: SetCalendarWidgetEnabledUseCase,
    private val context: Application,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(
        showCanChiOnCell = getShowCanChiOnCell().getOrDefault(false),
        calendarWidgetEnabled = getCalendarWidgetEnabled().getOrDefault(false),
        appVersion = BuildConfig.VERSION_NAME,
    )
) {

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToggleCanChiOnCell -> {
                updateState { copy(showCanChiOnCell = intent.enabled) }
                setShowCanChiOnCell(intent.enabled).onFailure { updateState { copy(showCanChiOnCell = !intent.enabled) } }
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
                setCalendarWidgetEnabled(true)
                sendEffect(SettingsEffect.ShowMessage("Thêm widget thành công!"))
            }.onFailure {
                setCalendarWidgetEnabled(false)
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
