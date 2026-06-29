package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state

sealed interface SettingsIntent {
    data class ToggleCanChiOnCell(val enabled: Boolean) : SettingsIntent
}