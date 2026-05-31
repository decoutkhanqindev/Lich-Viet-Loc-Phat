package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.ConvertResultUiModel

object ConverterContract {

    @Immutable
    data class State(
        val mode: ConvertMode = ConvertMode.SOLAR_TO_LUNAR,
        val inputDay: Int = 1,
        val inputMonth: Int = 1,
        val inputYear: Int = SolarDate.today().year,
        val isLeapMonth: Boolean = false,
        val result: ConvertResultUiModel? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed class Intent {
        data class ChangeMode(val mode: ConvertMode) : Intent()
        data class InputChanged(val day: Int, val month: Int, val year: Int) : Intent()
        data class ToggleLeapMonth(val checked: Boolean) : Intent()
        data object Convert : Intent()
    }

    sealed class Effect {
        data object ScrollResultIntoView : Effect()
    }
}
