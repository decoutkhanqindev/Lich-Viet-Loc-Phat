package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state

sealed interface MainIntent {
    data object OpenNetworkSettings : MainIntent
}
