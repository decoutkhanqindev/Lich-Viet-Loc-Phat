package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state

sealed interface SettingsIntent {
    data class ToggleCanChiOnCell(val enabled: Boolean) : SettingsIntent
}