package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state

sealed interface SettingsEffect {
    data class ShowMessage(val message: String) : SettingsEffect
}