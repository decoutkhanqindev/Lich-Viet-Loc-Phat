package com.decoutkhanqindev.lich_viet_loc_phat.device.repository

import com.decoutkhanqindev.lich_viet_loc_phat.device.hardware.InternetHardware
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class DeviceRepositoryImpl(
    private val internetHardware: InternetHardware,
) : DeviceRepository {
    override fun observeNetworkStatus(): Flow<Boolean> =
        internetHardware.observeNetworkStatus()
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
}