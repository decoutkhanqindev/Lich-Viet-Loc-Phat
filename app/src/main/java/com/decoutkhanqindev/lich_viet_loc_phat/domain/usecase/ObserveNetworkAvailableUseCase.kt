package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.DeviceRepository
import com.decoutkhanqindev.lich_viet_loc_phat.utils.toFlowResult
import kotlinx.coroutines.flow.Flow

class ObserveNetworkAvailableUseCase(private val repository: DeviceRepository) {
    operator fun invoke(): Flow<Result<Boolean>> = repository.observeNetworkAvailable().toFlowResult()
}
