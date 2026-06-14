package com.decoutkhanqindev.lich_viet_loc_phat.device.hardware

import kotlinx.coroutines.flow.Flow

interface InternetHardware {
    fun observeNetworkStatus(): Flow<Boolean>
}

