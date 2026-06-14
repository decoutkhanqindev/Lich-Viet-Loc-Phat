package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.DayCellUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CalendarState(
    val isLoading: Boolean = true,
    val displayedYear: Int = SolarDate.today().year,
    val displayedMonth: Int = SolarDate.today().month,
    val days: ImmutableList<DayCellUiModel> = persistentListOf(),
    val selectedDate: SolarDate? = null,
    val showTodayButton: Boolean = false,
    val error: String? = null,
    val showCanChiOnCell: Boolean = true,
    val lunarYearLabel: String? = null,
    val lunarMonthLabel: String? = null,
    val showMonthYearPicker: Boolean = false,
)