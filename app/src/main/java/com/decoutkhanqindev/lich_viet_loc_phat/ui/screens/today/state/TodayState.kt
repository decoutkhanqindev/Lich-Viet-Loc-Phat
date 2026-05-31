package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DailyMetadataUiModel

@Immutable
data class TodayState(
    val isLoading: Boolean = true,
    val selectedDate: SolarDate = SolarDate.today(),
    val dailyMetadata: DailyMetadataUiModel? = null,
    val error: String? = null,
)