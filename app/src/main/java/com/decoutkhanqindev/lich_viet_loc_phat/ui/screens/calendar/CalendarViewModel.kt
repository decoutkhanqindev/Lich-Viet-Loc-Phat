package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.toUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarEffect
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarState
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
    private val prefs: SharedPreferences,
) : ViewModel() {

    companion object {
        private const val KEY_SHOW_CAN_CHI_ON_CELL = "show_can_chi_on_cell"
    }

    private val initDate = SolarDate.today()

    private val _state = MutableStateFlow(
        CalendarState(
            displayedYear = initDate.year,
            displayedMonth = initDate.month,
            showCanChiOnCell = prefs.getBoolean(KEY_SHOW_CAN_CHI_ON_CELL, true),
        )
    )
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == KEY_SHOW_CAN_CHI_ON_CELL) {
            _state.update { it.copy(showCanChiOnCell = prefs.getBoolean(key, true)) }
        }
    }

    private val _effect = Channel<CalendarEffect>(Channel.BUFFERED)
    val effect: Flow<CalendarEffect> = _effect.receiveAsFlow()

    private var loadMonthJob: Job? = null

    init {
        prefs.registerOnSharedPreferenceChangeListener(prefsListener)
        loadMonth(initDate.year, initDate.month)
    }

    fun onIntent(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.SelectDay -> {
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
                    _effect.send(CalendarEffect.NavigateToToday(date))
                }
            }

            is CalendarIntent.PrevMonth -> {
                val (y, m) = prevMonth(_state.value.displayedYear, _state.value.displayedMonth)
                _state.update { it.copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarIntent.NextMonth -> {
                val (y, m) = nextMonth(_state.value.displayedYear, _state.value.displayedMonth)
                _state.update { it.copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarIntent.RequestToday -> {
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
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            val selected = _state.value.selectedDate
            getDaysInMonth(year, month)
                .onSuccess { cells ->
                    val firstCurrent = cells.firstOrNull { it.isCurrentMonth }
                    val lunarYearLabel = firstCurrent?.canChi?.let { "${it.canNam} ${it.chiNam}" }
                    val lunarMonthLabel = firstCurrent?.canChi?.let { "${it.canThang} ${it.chiThang}" }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            days = cells.map { c ->
                                c.toUiModel(selected)
                            }.toImmutableList(),
                            lunarYearLabel = lunarYearLabel,
                            lunarMonthLabel = lunarMonthLabel,
                        )
                    }
                }
                .onFailure { err ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = err.message ?: "Không tải được lịch"
                        )
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


    override fun onCleared() {
        prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
        _effect.close()
        super.onCleared()
    }
}
