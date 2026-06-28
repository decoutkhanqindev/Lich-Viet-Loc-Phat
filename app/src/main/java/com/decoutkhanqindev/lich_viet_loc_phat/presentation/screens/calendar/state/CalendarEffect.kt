package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarEffect {
    @Immutable
    data class NavigateToToday(val date: SolarDate) : CalendarEffect

    data object WatchAdToAddWidget : CalendarEffect

    @Immutable
    data class ShowMessage(@param:StringRes val messageRes: Int) : CalendarEffect
}