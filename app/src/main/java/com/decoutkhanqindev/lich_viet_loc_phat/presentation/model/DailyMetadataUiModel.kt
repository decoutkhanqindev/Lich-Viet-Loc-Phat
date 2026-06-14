package com.decoutkhanqindev.lich_viet_loc_phat.presentation.model

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DailyMetadata
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class DailyMetadataUiModel(
    val solar: SolarDate,
    val lunar: LunarDate,
    val canChi: CanChi,
    val auspiciousHours: ImmutableList<HourInfoUiModel>,
    val solarTerm: String?,
    val holiday: String?,
)

fun DailyMetadata.toUiModel() = DailyMetadataUiModel(
    solar = solar,
    lunar = lunar,
    canChi = canChi,
    auspiciousHours = auspiciousHours.map { it.toUiModel() }.toImmutableList(),
    solarTerm = solarTerm,
    holiday = holiday,
)
