package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DailyMetadataUiModel

object TodayContract {

    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val selectedDate: SolarDate = SolarDate.today(),
        val dailyMetadata: DailyMetadataUiModel? = null,
        val error: String? = null,
    )

    sealed class Intent {
        data class SelectDate(val date: SolarDate) : Intent()
        data object RequestToday : Intent()
        data object NavigateToPrevDay : Intent()
        data object NavigateToNextDay : Intent()
    }

}
