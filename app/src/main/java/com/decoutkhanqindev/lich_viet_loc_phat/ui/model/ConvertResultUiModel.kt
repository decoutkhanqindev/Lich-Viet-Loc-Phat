package com.decoutkhanqindev.lich_viet_loc_phat.ui.model

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi

@Immutable
data class ConvertResultUiModel(
    val dayLabel: String,
    val monthLabel: String,
    val yearLabel: String,
    val canChi: CanChi?,
    val leapMonthNote: String?,
)
