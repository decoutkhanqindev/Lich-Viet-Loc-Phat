package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.GetDailyMetadataUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodayViewModel(
    private val getDailyMetadata: GetDailyMetadataUseCase,
    initialDate: SolarDate? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(TodayContract.State())
    val state: StateFlow<TodayContract.State> = _state.asStateFlow()

    private var loadMetadataJob: Job? = null

    init {
        loadMetadata(initialDate ?: SolarDate.today())
    }

    fun onIntent(intent: TodayContract.Intent) {
        when (intent) {
            is TodayContract.Intent.SelectDate -> loadMetadata(intent.date)
            is TodayContract.Intent.RequestToday -> loadMetadata(SolarDate.today())
            is TodayContract.Intent.NavigateToPrevDay -> loadMetadata(
                _state.value.selectedDate.minusDays(1)
            )

            is TodayContract.Intent.NavigateToNextDay -> loadMetadata(
                _state.value.selectedDate.plusDays(1)
            )
        }
    }

    private fun loadMetadata(date: SolarDate) {
        loadMetadataJob?.cancel()
        loadMetadataJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, selectedDate = date) }
            getDailyMetadata(date)
                .onSuccess { metadata ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            dailyMetadata = metadata.toUiModel()
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Lỗi không xác định"
                        )
                    }
                }
        }
    }
}
