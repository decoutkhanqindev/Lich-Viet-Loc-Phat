package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class AdUnit(val id: String, val name: String) {
    protected val _state = MutableStateFlow(AdUnitState.NONE)
    val state: StateFlow<AdUnitState> = _state.asStateFlow()

    abstract fun load(context: Context)
    abstract fun pause()
    abstract fun resume()
    abstract fun destroy()
}
