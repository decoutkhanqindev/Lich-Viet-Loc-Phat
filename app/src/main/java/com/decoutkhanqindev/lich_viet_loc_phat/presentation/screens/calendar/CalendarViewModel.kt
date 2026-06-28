package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar

import android.app.Application
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.device.SharedPrefsManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDaysInMonthUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.toUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidget
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.widget.CalendarWidgetReceiver
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val getDaysInMonth: GetDaysInMonthUseCase,
    private val sharedPrefs: SharedPrefsManager,
    private val application: Application,
) : BaseViewModel<CalendarState, CalendarIntent, CalendarEffect>(
    initialState = SolarDate.today().let { today ->
        CalendarState(
            displayedYear = today.year,
            displayedMonth = today.month,
            showCanChiOnCell = sharedPrefs.showCanChiOnCell,
        )
    }
) {

    private val initDate = SolarDate.today()
    private var loadMonthJob: Job? = null

    init {
        viewModelScope.launch {
            sharedPrefs.showCanChiOnCellFlow.collect { show ->
                updateState { copy(showCanChiOnCell = show) }
            }
        }

        loadMonth(initDate.year, initDate.month)
    }

    override fun onIntent(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.SelectDay -> {
                val date = intent.date
                val isOverflow =
                    date.month != state.value.displayedMonth || date.year != state.value.displayedYear

                updateState { copy(selectedDate = date) }

                if (isOverflow) {
                    updateState { copy(displayedYear = date.year, displayedMonth = date.month) }
                    loadMonth(date.year, date.month)
                }

                viewModelScope.launch { sendEffect(CalendarEffect.NavigateToToday(date)) }
            }

            is CalendarIntent.PrevMonth -> {
                val (y, m) = prevMonth(state.value.displayedYear, state.value.displayedMonth)

                updateState { copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarIntent.NextMonth -> {
                val (y, m) = nextMonth(state.value.displayedYear, state.value.displayedMonth)

                updateState { copy(displayedYear = y, displayedMonth = m) }
                loadMonth(y, m)
                updateTodayButtonVisibility(y, m)
            }

            is CalendarIntent.RequestToday -> {
                val now = SolarDate.today()

                updateState {
                    copy(
                        displayedYear = now.year,
                        displayedMonth = now.month,
                        showTodayButton = false
                    )
                }
                loadMonth(now.year, now.month)
            }

            is CalendarIntent.ShowMonthYearPicker -> updateState { copy(showMonthYearPicker = true) }

            is CalendarIntent.DismissMonthYearPicker -> updateState { copy(showMonthYearPicker = false) }

            is CalendarIntent.ConfirmMonthYear -> {
                updateState {
                    copy(
                        showMonthYearPicker = false,
                        displayedYear = intent.year,
                        displayedMonth = intent.month,
                    )
                }
                loadMonth(intent.year, intent.month)
                updateTodayButtonVisibility(intent.year, intent.month)
            }

            is CalendarIntent.ShowWidgetBottomSheet -> updateState { copy(showWidgetBottomSheet = true) }

            is CalendarIntent.DismissWidgetBottomSheet -> updateState { copy(showWidgetBottomSheet = false) }

            is CalendarIntent.WatchAdToAddWidget -> {
                viewModelScope.launch { sendEffect(CalendarEffect.WatchAdToAddWidget) }
            }

            is CalendarIntent.AddWidget -> pinWidget()
        }
    }

    private fun loadMonth(year: Int, month: Int) {
        loadMonthJob?.cancel()
        loadMonthJob = viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            val selected = state.value.selectedDate
            getDaysInMonth(year, month).onSuccess { cells ->
                val firstCurrent = cells.firstOrNull { it.isCurrentMonth }
                val lunarYearLabel = firstCurrent?.canChi?.let { "${it.canNam} ${it.chiNam}" }
                val lunarMonthLabel =
                    firstCurrent?.canChi?.let { "${it.canThang} ${it.chiThang}" }

                delay(LOAD_MONTH_DEBOUNCE_DURATION)

                updateState {
                    copy(
                        isLoading = false,
                        days = cells.map { c -> c.toUiModel(selected) }.toImmutableList(),
                        lunarYearLabel = lunarYearLabel,
                        lunarMonthLabel = lunarMonthLabel,
                    )
                }
            }.onFailure { err ->
                updateState {
                    copy(isLoading = false, error = err.message ?: "Không tải được lịch")
                }
            }
        }
    }

    private fun updateTodayButtonVisibility(year: Int, month: Int) {
        val now = SolarDate.today()
        updateState { copy(showTodayButton = year != now.year || month != now.month) }
    }

    private fun prevMonth(year: Int, month: Int): Pair<Int, Int> =
        if (month == 1) year - 1 to 12 else year to month - 1

    private fun nextMonth(year: Int, month: Int): Pair<Int, Int> =
        if (month == 12) year + 1 to 1 else year to month + 1

    private fun pinWidget() {
        viewModelScope.launch {
            if (isWidgetPinned()) {
                sendEffect(CalendarEffect.ShowMessage(R.string.widget_pin_already_added))
                return@launch
            }

            runCatching {
                val isSupported = GlanceAppWidgetManager(application).requestPinGlanceAppWidget(
                    receiver = CalendarWidgetReceiver::class.java,
                    preview = CalendarWidget(),
                )
                if (!isSupported) {
                    sendEffect(CalendarEffect.ShowMessage(R.string.widget_pin_not_supported))
                }
            }.onSuccess {
                sendEffect(CalendarEffect.ShowMessage(R.string.widget_pin_success))
            }.onFailure {
                sendEffect(CalendarEffect.ShowMessage(R.string.widget_pin_failure))
            }
        }
    }

    private suspend fun isWidgetPinned(): Boolean =
        GlanceAppWidgetManager(application)
            .getGlanceIds(CalendarWidget::class.java)
            .isNotEmpty()

    companion object {
        private const val LOAD_MONTH_DEBOUNCE_DURATION = 300L
    }
}
