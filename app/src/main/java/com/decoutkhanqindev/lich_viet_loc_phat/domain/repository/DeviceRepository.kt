package com.decoutkhanqindev.lich_viet_loc_phat.domain.repository

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeNetworkStatus(): Flow<Boolean>
}