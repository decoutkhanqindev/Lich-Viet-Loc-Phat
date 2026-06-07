package com.decoutkhanqindev.lich_viet_loc_phat.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getShowCanChiOnCell(): Boolean
    fun setShowCanChiOnCell(enabled: Boolean)
    fun observeShowCanChiOnCell(): Flow<Boolean>

    fun getCalendarWidgetEnabled(): Boolean
    fun setCalendarWidgetEnabled(enabled: Boolean)
}
