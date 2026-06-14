package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state

import androidx.compose.runtime.Immutable

sealed interface MainIntent {
    data object OpenNetworkSettings : MainIntent
}
