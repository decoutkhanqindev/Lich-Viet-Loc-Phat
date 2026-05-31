package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import androidx.compose.runtime.Immutable

object SettingsContract {

    @Immutable
    data class State(
        val showCanChiOnCell: Boolean = true,
        val appVersion: String = "",
    )

    sealed class Intent {
        data class ToggleCanChiOnCell(val enabled: Boolean) : Intent()
    }

}
