package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.ConvertResultUiModel

@Immutable
data class ConverterState(
    val mode: ConvertMode = ConvertMode.SOLAR_TO_LUNAR,
    val inputDay: Int = 1,
    val inputMonth: Int = 1,
    val inputYear: Int = SolarDate.today().year,
    val isLeapMonth: Boolean = false,
    val result: ConvertResultUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)