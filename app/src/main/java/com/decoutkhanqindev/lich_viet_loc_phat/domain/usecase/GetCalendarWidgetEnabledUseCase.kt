package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.SettingsRepository

class GetCalendarWidgetEnabledUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Result<Boolean> = runCatching {
        repository.getCalendarWidgetEnabled()
    }
}
