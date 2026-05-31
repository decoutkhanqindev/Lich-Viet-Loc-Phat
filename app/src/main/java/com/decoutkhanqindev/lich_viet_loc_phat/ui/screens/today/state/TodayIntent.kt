package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface TodayIntent {
    data object RequestToday : TodayIntent
    data object NavigateToPrevDay : TodayIntent
    data object NavigateToNextDay : TodayIntent
}