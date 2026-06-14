package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarIntent {
    @Immutable
    data class SelectDay(val date: SolarDate) : CalendarIntent

    data object PrevMonth : CalendarIntent

    data object NextMonth : CalendarIntent

    data object RequestToday : CalendarIntent

    data object ShowMonthYearPicker : CalendarIntent

    data object DismissMonthYearPicker : CalendarIntent

    @Immutable
    data class ConfirmMonthYear(val year: Int, val month: Int) : CalendarIntent
}