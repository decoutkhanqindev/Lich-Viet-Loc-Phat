package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository

class GetHourlyAuspiciousnessUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(date: SolarDate): Result<List<HourInfo>> =
        runCatching { repository.getAuspiciousHours(date) }
}
