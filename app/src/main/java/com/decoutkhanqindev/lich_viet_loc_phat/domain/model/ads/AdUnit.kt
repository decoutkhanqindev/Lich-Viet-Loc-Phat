package com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ads

import android.content.Context
import com.decoutkhanqindev.lich_viet_loc_phat.utils.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class AdUnit(val id: String, val name: String) : Tag {
    protected val _state = MutableStateFlow(AdUnitState.NONE)
    val state: StateFlow<AdUnitState> = _state.asStateFlow()

    abstract fun load(context: Context)
    abstract fun destroy()
}
