package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsState(
    val showCanChiOnCell: Boolean = true,
    val appVersion: String = "",
)