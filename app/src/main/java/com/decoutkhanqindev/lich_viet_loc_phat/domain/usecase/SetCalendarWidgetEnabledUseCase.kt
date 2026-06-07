package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.SettingsRepository

class SetCalendarWidgetEnabledUseCase(private val repository: SettingsRepository) {
    operator fun invoke(enabled: Boolean): Result<Unit> = runCatching {
        repository.setCalendarWidgetEnabled(enabled)
    }
}
