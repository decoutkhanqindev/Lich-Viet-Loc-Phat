package com.decoutkhanqindev.lich_viet_loc_phat.ui.model

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DayCell
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

@Immutable
data class DayCellUiModel(
    val solar: SolarDate,
    val lunar: LunarDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val holiday: String?,
    val canChiLabel: String,
    val solarTerm: String?,
)

fun DayCell.toUiModel(selectedDate: SolarDate? = null) = DayCellUiModel(
    solar = solar,
    lunar = lunar,
    isCurrentMonth = isCurrentMonth,
    isToday = isToday,
    isSelected = selectedDate != null && solar == selectedDate,
    holiday = holiday,
    canChiLabel = "${canChi.canNgay} ${canChi.chiNgay}",
    solarTerm = solarTerm,
)
