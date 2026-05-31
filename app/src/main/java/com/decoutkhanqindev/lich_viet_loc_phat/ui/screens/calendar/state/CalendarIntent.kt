package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarIntent {
    data class SelectDay(val date: SolarDate) : CalendarIntent
    data object PrevMonth : CalendarIntent
    data object NextMonth : CalendarIntent
    data object RequestToday : CalendarIntent
}