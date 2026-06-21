package com.decoutkhanqindev.lich_viet_loc_phat.device

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class SharedPrefsManager(context: Context) {
    private val sharedPrefs by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var showCanChiOnCell: Boolean
        get() = sharedPrefs.getBoolean(KEY_SHOW_CAN_CHI_ON_CELL, false)
        set(value) {
            sharedPrefs.edit { putBoolean(KEY_SHOW_CAN_CHI_ON_CELL, value) }
        }

    val showCanChiOnCellFlow: Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_SHOW_CAN_CHI_ON_CELL) trySend(showCanChiOnCell)
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(showCanChiOnCell)
        awaitClose { sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    var calendarWidgetEnabled: Boolean
        get() = sharedPrefs.getBoolean(KEY_CALENDAR_WIDGET_ENABLED, false)
        set(value) {
            sharedPrefs.edit { putBoolean(KEY_CALENDAR_WIDGET_ENABLED, value) }
        }

    var interHomeTabCount: Int
        get() = sharedPrefs.getInt(KEY_INTER_HOME_TAB_COUNT, 2)
        set(value) {
            sharedPrefs.edit { putInt(KEY_INTER_HOME_TAB_COUNT, value) }
        }

    var interHomeInterval: Long
        get() = sharedPrefs.getLong(KEY_INTER_HOME_INTERVAL, 60_000L)
        set(value) {
            sharedPrefs.edit { putLong(KEY_INTER_HOME_INTERVAL, value) }
        }

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_SHOW_CAN_CHI_ON_CELL = "show_can_chi_on_cell"
        private const val KEY_CALENDAR_WIDGET_ENABLED = "calendar_widget_enabled"
        private const val KEY_INTER_HOME_TAB_COUNT = "inter_home_tab_count"
        private const val KEY_INTER_HOME_INTERVAL = "inter_home_interval"
    }
}
