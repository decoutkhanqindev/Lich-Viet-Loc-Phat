package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

data class DailyMetadata(
    val solar: SolarDate,
    val lunar: LunarDate,
    val canChi: CanChi,
    val auspiciousHours: List<HourInfo>,
    val solarTerm: String?,
)
