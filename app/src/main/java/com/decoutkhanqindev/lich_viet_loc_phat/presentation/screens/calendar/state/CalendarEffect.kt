package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state

import androidx.annotation.StringRes
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarEffect {
    data class NavigateToToday(val date: SolarDate) : CalendarEffect

    data object WatchAdToAddWidget : CalendarEffect

    data class ShowMessage(@param:StringRes val messageRes: Int) : CalendarEffect
}