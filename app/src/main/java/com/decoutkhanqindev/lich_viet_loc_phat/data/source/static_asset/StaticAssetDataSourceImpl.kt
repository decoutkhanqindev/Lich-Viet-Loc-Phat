package com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset

import com.decoutkhanqindev.lich_viet_loc_phat.data.source.LunarMath
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

class StaticAssetDataSourceImpl : StaticAssetDataSource {

    override fun getSolarTerm(solar: SolarDate): String? {
        val jd = LunarMath.jdFromDate(solar.day, solar.month, solar.year)
        val longToday = LunarMath.sunLongitudeDeg(jd - 0.5 - VN_TZ / 24.0)
        val longYesterday = LunarMath.sunLongitudeDeg(jd - 1.0 - 0.5 - VN_TZ / 24.0)
        val zoneToday = (longToday / 15.0).toInt()
        val zoneYesterday = (longYesterday / 15.0).toInt()
        return if (zoneToday != zoneYesterday) SOLAR_TERMS[zoneToday % 24] else null
    }

    override fun getHoliday(solar: SolarDate, lunar: LunarDate): String? {
        val solarHoliday = when (solar.month) {
            1 if solar.day == 1 -> "Tết Dương Lịch"
            4 if solar.day == 30 -> "Giải Phóng Miền Nam"
            5 if solar.day == 1 -> "Quốc Tế Lao Động"
            9 if solar.day == 2 -> "Quốc Khánh"
            else -> null
        }
        if (solarHoliday != null) return solarHoliday
        return when {
            !lunar.isLeapMonth && lunar.month == 1 && lunar.day in 1..5 -> "Tết Nguyên Đán"
            !lunar.isLeapMonth && lunar.month == 3 && lunar.day == 10 -> "Giỗ Tổ Hùng Vương"
            else -> null
        }
    }

    companion object {
        private const val VN_TZ = 7.0

        // 24 solar terms mapped by sun longitude zone index (longitude / 15)
        // Index 0 = 0° (Xuân phân), index 21 = 315° (Lập xuân), etc.
        private val SOLAR_TERMS by lazy {
            arrayOf(
                "Xuân Phân",    //  0°
                "Thanh Minh",   // 15°
                "Cốc Vũ",       // 30°
                "Lập Hạ",       // 45°
                "Tiểu Mãn",     // 60°
                "Mang Chủng",   // 75°
                "Hạ Chí",       // 90°
                "Tiểu Thử",     // 105°
                "Đại Thử",      // 120°
                "Lập Thu",      // 135°
                "Xử Thử",       // 150°
                "Bạch Lộ",      // 165°
                "Thu Phân",     // 180°
                "Hàn Lộ",       // 195°
                "Sương Giáng",  // 210°
                "Lập Đông",     // 225°
                "Tiểu Tuyết",   // 240°
                "Đại Tuyết",    // 255°
                "Đông Chí",     // 270°
                "Tiểu Hàn",     // 285°
                "Đại Hàn",      // 300°
                "Lập Xuân",     // 315°
                "Vũ Thủy",      // 330°
                "Kinh Trập",    // 345°
            )
        }
    }
}
