package com.decoutkhanqindev.lich_viet_loc_phat.domain.repository

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DailyMetadata
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DayCell
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

interface CalendarRepository {
    suspend fun getDailyMetadata(date: SolarDate): DailyMetadata
    suspend fun getDaysInMonth(year: Int, month: Int): List<DayCell>
    suspend fun convertSolarToLunar(solar: SolarDate): LunarDate
    suspend fun convertLunarToSolar(lunar: LunarDate): SolarDate
    suspend fun calculateCanChi(date: SolarDate): CanChi
    suspend fun getAuspiciousHours(date: SolarDate): List<HourInfo>
    suspend fun getSolarTerm(date: SolarDate): String?
}
