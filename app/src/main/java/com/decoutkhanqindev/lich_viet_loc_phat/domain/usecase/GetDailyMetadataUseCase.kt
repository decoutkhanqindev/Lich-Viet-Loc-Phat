package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DailyMetadata
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository

class GetDailyMetadataUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(date: SolarDate): Result<DailyMetadata> = runCatching {
        repository.getDailyMetadata(date)
    }
}
