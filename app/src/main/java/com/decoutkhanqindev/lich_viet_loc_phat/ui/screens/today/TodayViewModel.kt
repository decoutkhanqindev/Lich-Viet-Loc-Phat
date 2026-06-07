package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDailyMetadataUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.toUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state.TodayIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state.TodayState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodayViewModel(
    private val getDailyMetadata: GetDailyMetadataUseCase,
) : BaseViewModel<TodayState, TodayIntent, Nothing>(TodayState()) {

    private var loadMetadataJob: Job? = null

    init {
        loadMetadata(SolarDate.today())
    }

    fun setInitialDate(date: SolarDate) {
        loadMetadata(date)
    }

    override fun onIntent(intent: TodayIntent) {
        when (intent) {
            is TodayIntent.RequestToday -> loadMetadata(SolarDate.today())
            is TodayIntent.NavigateToPrevDay -> loadMetadata(state.value.selectedDate.minusDays(1))
            is TodayIntent.NavigateToNextDay -> loadMetadata(state.value.selectedDate.plusDays(1))
        }
    }

    private fun loadMetadata(date: SolarDate) {
        val today = SolarDate.today()
        loadMetadataJob?.cancel()
        loadMetadataJob = viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    error = null,
                    selectedDate = date,
                    showTodayButton = date != today,
                )
            }
            getDailyMetadata(date)
                .onSuccess { metadata ->
                    updateState { copy(isLoading = false, dailyMetadata = metadata.toUiModel()) }
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message ?: "Lỗi không xác định") }
                }
        }
    }
}
