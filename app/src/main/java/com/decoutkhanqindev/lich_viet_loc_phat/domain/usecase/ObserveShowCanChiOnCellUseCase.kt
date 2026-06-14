package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.SettingsRepository
import com.decoutkhanqindev.lich_viet_loc_phat.utils.toFlowResult
import kotlinx.coroutines.flow.Flow

class ObserveShowCanChiOnCellUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Result<Boolean>> = repository.observeShowCanChiOnCell().toFlowResult()
}
