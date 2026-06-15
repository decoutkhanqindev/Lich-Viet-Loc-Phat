package com.decoutkhanqindev.lich_viet_loc_phat.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SettingsRepositoryImpl(private val prefs: SharedPreferences) : SettingsRepository {

    override fun getShowCanChiOnCell(): Boolean =
        prefs.getBoolean(KEY_SHOW_CAN_CHI_ON_CELL, false)

    override fun setShowCanChiOnCell(enabled: Boolean) =
        prefs.edit { putBoolean(KEY_SHOW_CAN_CHI_ON_CELL, enabled) }

    override fun observeShowCanChiOnCell(): Flow<Boolean> = callbackFlow {
        trySend(getShowCanChiOnCell())
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_SHOW_CAN_CHI_ON_CELL) trySend(getShowCanChiOnCell())
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun getCalendarWidgetEnabled(): Boolean =
        prefs.getBoolean(KEY_CALENDAR_WIDGET_ENABLED, false)

    override fun setCalendarWidgetEnabled(enabled: Boolean) =
        prefs.edit { putBoolean(KEY_CALENDAR_WIDGET_ENABLED, enabled) }

    companion object {
        private const val KEY_SHOW_CAN_CHI_ON_CELL = "show_can_chi_on_cell"
        private const val KEY_CALENDAR_WIDGET_ENABLED = "calendar_widget_enabled"
    }
}
