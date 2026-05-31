package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

import java.time.LocalDate
import java.time.ZoneId

data class SolarDate(val day: Int, val month: Int, val year: Int) {

    fun minusDays(n: Int): SolarDate {
        val result = LocalDate.of(year, month, day).minusDays(n.toLong())
        return SolarDate(result.dayOfMonth, result.monthValue, result.year)
    }

    fun plusDays(n: Int): SolarDate {
        val result = LocalDate.of(year, month, day).plusDays(n.toLong())
        return SolarDate(result.dayOfMonth, result.monthValue, result.year)
    }

    companion object {
        private val VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh")

        fun today(): SolarDate {
            val now = LocalDate.now(VN_ZONE)
            return SolarDate(now.dayOfMonth, now.monthValue, now.year)
        }
    }
}
