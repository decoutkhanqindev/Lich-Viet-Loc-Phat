package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

sealed interface CalendarIntent {
    data class SelectDay(val date: SolarDate) : CalendarIntent

    data object PrevMonth : CalendarIntent

    data object NextMonth : CalendarIntent

    data object RequestToday : CalendarIntent

    data object ShowMonthYearPicker : CalendarIntent

    data object DismissMonthYearPicker : CalendarIntent

    data class ConfirmMonthYear(val year: Int, val month: Int) : CalendarIntent

    data object ShowWidgetBottomSheet : CalendarIntent

    data object DismissWidgetBottomSheet : CalendarIntent

    data object WatchAdToAddWidget : CalendarIntent

    data object AddWidget : CalendarIntent
}