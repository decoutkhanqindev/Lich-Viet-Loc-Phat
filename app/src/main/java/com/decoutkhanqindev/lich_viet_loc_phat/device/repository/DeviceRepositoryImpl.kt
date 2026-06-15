package com.decoutkhanqindev.lich_viet_loc_phat.device.repository

import com.decoutkhanqindev.lich_viet_loc_phat.device.hardware.NetworkManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class DeviceRepositoryImpl(
    private val networkManager: NetworkManager,
) : DeviceRepository {
    override fun observeNetworkAvailable(): Flow<Boolean> =
        networkManager.observeNetworkAvailable()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
}