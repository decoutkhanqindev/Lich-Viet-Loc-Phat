package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DayCellUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object CalendarContract {

    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val displayedYear: Int = SolarDate.today().year,
        val displayedMonth: Int = SolarDate.today().month,
        val days: ImmutableList<DayCellUiModel> = persistentListOf(),
        val selectedDate: SolarDate? = null,
        val showTodayButton: Boolean = false,
        val error: String? = null,
    )

    sealed class Intent {
        data class SelectDay(val date: SolarDate) : Intent()
        data object PrevMonth : Intent()
        data object NextMonth : Intent()
        data object RequestToday : Intent()
    }

    sealed class Effect {
        data class NavigateToToday(val date: SolarDate) : Effect()
    }
}
