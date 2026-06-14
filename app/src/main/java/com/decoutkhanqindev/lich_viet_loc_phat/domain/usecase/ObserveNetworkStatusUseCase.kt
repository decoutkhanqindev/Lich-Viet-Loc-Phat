package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ObserveNetworkStatusUseCase(private val repo: DeviceRepository) {
    operator fun invoke(): Flow<Result<Boolean>> = try {
        repo.observeNetworkStatus().map { Result.success(it) }
    } catch (e: Exception) {
        flowOf(Result.failure(e))
    }
}
