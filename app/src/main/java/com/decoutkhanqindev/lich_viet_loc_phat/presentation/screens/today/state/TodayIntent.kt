package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.today.state

sealed interface TodayIntent {
    data object RequestToday : TodayIntent
    data object NavigateToPrevDay : TodayIntent
    data object NavigateToNextDay : TodayIntent
}