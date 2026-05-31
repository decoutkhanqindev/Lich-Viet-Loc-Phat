package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarEffect {
    data class NavigateToToday(val date: SolarDate) : CalendarEffect
}