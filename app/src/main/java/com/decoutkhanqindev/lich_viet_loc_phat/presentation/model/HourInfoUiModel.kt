package com.decoutkhanqindev.lich_viet_loc_phat.presentation.model

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo

@Immutable
data class HourInfoUiModel(
    val name: String,
    val timeRange: String,
    val isAuspicious: Boolean,
)

fun HourInfo.toUiModel() = HourInfoUiModel(
    name = name,
    timeRange = timeRange,
    isAuspicious = isAuspicious,
)
