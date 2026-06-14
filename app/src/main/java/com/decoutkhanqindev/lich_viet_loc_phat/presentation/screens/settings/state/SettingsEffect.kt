package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state

import androidx.compose.runtime.Immutable

sealed interface SettingsEffect {
    @Immutable
    data class ShowMessage(val message: String) : SettingsEffect
}