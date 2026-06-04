package com.decoutkhanqindev.lich_viet_loc_phat.navigation

import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import kotlinx.serialization.Serializable

// day/month/year == 0 → hôm nay; non-zero → ngày cụ thể từ Calendar
@Serializable
data class TodayDestination(val day: Int = 0, val month: Int = 0, val year: Int = 0) : NavKey {
    fun toSolarDate(): SolarDate? = if (day == 0) null else SolarDate(day, month, year)
}

@Serializable
data object CalendarDestination : NavKey

@Serializable
data object SettingsDestination : NavKey

fun SolarDate.toTodayDestination() = TodayDestination(day, month, year)
