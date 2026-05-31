package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.CalculateCanChiUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ConvertLunarToSolarUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ConvertSolarToLunarUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.ConvertResultUiModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConverterViewModel(
    private val convertSolarToLunar: ConvertSolarToLunarUseCase,
    private val convertLunarToSolar: ConvertLunarToSolarUseCase,
    private val calculateCanChi: CalculateCanChiUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ConverterContract.State())
    val state: StateFlow<ConverterContract.State> = _state.asStateFlow()

    private val _effect = Channel<ConverterContract.Effect>(Channel.BUFFERED)
    val effect: Flow<ConverterContract.Effect> = _effect.receiveAsFlow()

    fun onIntent(intent: ConverterContract.Intent) {
        when (intent) {
            is ConverterContract.Intent.ChangeMode -> _state.update {
                it.copy(mode = intent.mode, result = null, error = null, isLeapMonth = false)
            }

            is ConverterContract.Intent.InputChanged -> _state.update {
                it.copy(
                    inputDay = intent.day,
                    inputMonth = intent.month,
                    inputYear = intent.year,
                    result = null,
                    error = null
                )
            }

            is ConverterContract.Intent.ToggleLeapMonth -> _state.update {
                it.copy(isLeapMonth = intent.checked)
            }

            is ConverterContract.Intent.Convert -> performConvert()
        }
    }

    private fun performConvert() {
        if (_state.value.isLoading) return
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                when (s.mode) {
                    ConvertMode.SOLAR_TO_LUNAR -> {
                        val solar = SolarDate(s.inputDay, s.inputMonth, s.inputYear)
                        val lunar = convertSolarToLunar(solar).getOrThrow()
                        val canChi = calculateCanChi(solar).getOrNull()
                        ConvertResultUiModel(
                            dayLabel = "${lunar.day}",
                            monthLabel = "${lunar.month}${if (lunar.isLeapMonth) " (Nhuận)" else ""}",
                            yearLabel = "${lunar.year}",
                            canChi = canChi,
                            leapMonthNote = if (lunar.isLeapMonth) "Tháng Nhuận" else null,
                        )
                    }

                    ConvertMode.LUNAR_TO_SOLAR -> {
                        val lunar = LunarDate(s.inputDay, s.inputMonth, s.inputYear, s.isLeapMonth)
                        val solar = convertLunarToSolar(lunar).getOrThrow()
                        val canChi = calculateCanChi(solar).getOrNull()
                        ConvertResultUiModel(
                            dayLabel = "${solar.day}",
                            monthLabel = "${solar.month}",
                            yearLabel = "${solar.year}",
                            canChi = canChi,
                            leapMonthNote = null,
                        )
                    }
                }
            }.onSuccess { result ->
                _state.update { it.copy(isLoading = false, result = result) }
                _effect.send(ConverterContract.Effect.ScrollResultIntoView)
            }.onFailure { err ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = err.message ?: "Ngày không hợp lệ"
                    )
                }
            }
        }
    }
}
