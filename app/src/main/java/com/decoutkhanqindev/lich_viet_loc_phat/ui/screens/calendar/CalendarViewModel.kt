package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.toUiModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val getDaysInMonth: GetDaysInMonthUseCase,
) : ViewModel() {

    private val initDate = SolarDate.today()

    private val _state = MutableStateFlow(
        CalendarContract.State(displayedYear = initDate.year, displayedMonth = initDate.month)
    )
    val state: StateFlow<CalendarContract.State> = _state.asStateFlow()

    private val _effect = Channel<CalendarContract.Effect>(Channel.BUFFERED)
    val effect: Flow<CalendarContract.Effect> = _effect.receiveAsFlow()

    private var loadMonthJob: Job? = null

    init {
        loadMonth(initDate.year, initDate.month)
    }

    fun onIntent(intent: CalendarContract.Intent) {
        when (intent) {
            is CalendarContract.Intent.SelectDay -> {
                val date = intent.date
                val isOverflow =
                    date.month != _state.value.displayedMonth || date.year != _state.value.displayedYear
                _state.update { it.copy(selectedDate = date) }
                if (isOverflow) {
                    _state.update {
                        it.copy(
                            displayedYear = date.year,
                            displayedMonth = date.month
                        )
                    }
                    loadMonth(date.year, date.month)
                }
                viewModelScope.launch {
                    _effect.send(CalendarContract.Effect.NavigateToToday(date))
                }
            }

            is CalendarContract.Intent.PrevMonth -> {
                val (y, m) = prevMonth(_state.value.displayedYear, _state.value.displayedMonth)
                _state.update { it.copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarContract.Intent.NextMonth -> {
                val (y, m) = nextMonth(_state.value.displayedYear, _state.value.displayedMonth)
                _state.update { it.copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarContract.Intent.RequestToday -> {
                val now = SolarDate.today()
                _state.update {
                    it.copy(
                        displayedYear = now.year,
                        displayedMonth = now.month,
                        showTodayButton = false
                    )
                }
                loadMonth(now.year, now.month)
            }
        }
    }

    private fun loadMonth(year: Int, month: Int) {
        loadMonthJob?.cancel()
        loadMonthJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val selected = _state.value.selectedDate
            getDaysInMonth(year, month)
                .onSuccess { cells ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            days = cells.map { c -> c.toUiModel(selected) }.toImmutableList()
                        )
                    }
                }
                .onFailure { err ->
                    _state.update {
                        it.copy(isLoading = false, error = err.message ?: "Không tải được lịch")
                    }
                }
        }
    }

    private fun updateTodayButtonVisibility(year: Int, month: Int) {
        val now = SolarDate.today()
        _state.update { it.copy(showTodayButton = year != now.year || month != now.month) }
    }

    private fun prevMonth(year: Int, month: Int): Pair<Int, Int> =
        if (month == 1) year - 1 to 12 else year to month - 1

    private fun nextMonth(year: Int, month: Int): Pair<Int, Int> =
        if (month == 12) year + 1 to 1 else year to month + 1
}
