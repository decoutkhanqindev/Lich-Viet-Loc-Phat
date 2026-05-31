package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository

class ConvertLunarToSolarUseCase(private val repository: CalendarRepository) {
    // LunarDate.isLeapMonth xử lý tháng nhuận theo PRD Edge Case
    suspend operator fun invoke(lunar: LunarDate): Result<SolarDate> =
        runCatching { repository.convertLunarToSolar(lunar) }
}
