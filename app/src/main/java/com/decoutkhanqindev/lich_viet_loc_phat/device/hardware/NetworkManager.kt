package com.decoutkhanqindev.lich_viet_loc_phat.device.hardware

import kotlinx.coroutines.flow.Flow

interface NetworkManager {
    fun observeNetworkAvailable(): Flow<Boolean>
}

