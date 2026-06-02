package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

data class DayCell(
    val solar: SolarDate,
    val lunar: LunarDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val holiday: String?,
    val canChi: CanChi,
    val solarTerm: String?,
)
