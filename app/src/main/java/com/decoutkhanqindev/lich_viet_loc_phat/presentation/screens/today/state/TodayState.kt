package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.today.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.DailyMetadataUiModel

@Immutable
data class TodayState(
    val isLoading: Boolean = true,
    val selectedDate: SolarDate = SolarDate.today(),
    val showTodayButton: Boolean = false,
    val dailyMetadata: DailyMetadataUiModel? = null,
    val error: String? = null,
)