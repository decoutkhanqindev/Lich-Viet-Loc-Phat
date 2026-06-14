package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state

import androidx.compose.runtime.Immutable

sealed interface SettingsIntent {
    @Immutable
    data class ToggleCanChiOnCell(val enabled: Boolean) : SettingsIntent

    @Immutable
    data class ToggleCalendarWidget(val enabled: Boolean) : SettingsIntent
}