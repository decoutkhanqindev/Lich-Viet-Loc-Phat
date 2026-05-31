package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository

class GetSolarTermUseCase(private val repository: CalendarRepository) {
    // null nếu ngày đó không phải ngày khởi đầu Tiết khí
    suspend operator fun invoke(date: SolarDate): Result<String?> =
        runCatching { repository.getSolarTerm(date) }
}
