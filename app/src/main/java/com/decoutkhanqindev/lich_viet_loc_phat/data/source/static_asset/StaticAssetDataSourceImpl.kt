package com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset

import com.decoutkhanqindev.lich_viet_loc_phat.data.source.LunarMath
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

class StaticAssetDataSourceImpl : StaticAssetDataSource {

    override fun getSolarTerm(solar: SolarDate): String? {
        val jd = LunarMath.jdFromDate(
            dd = solar.day,
            mm = solar.month,
            yyyy = solar.year
        )
        val longToday = LunarMath.sunLongitudeDeg(jd - 0.5 - VN_TZ / 24.0)
        val longYesterday = LunarMath.sunLongitudeDeg(jd - 1.0 - 0.5 - VN_TZ / 24.0)
        val zoneToday = (longToday / 15.0).toInt()
        val zoneYesterday = (longYesterday / 15.0).toInt()
        return if (zoneToday != zoneYesterday) SOLAR_TERMS[zoneToday % 24] else null
    }

    override fun getHoliday(solar: SolarDate, lunar: LunarDate): String? {
        solarHoliday(solar)?.let { return it }
        if (lunar.isLeapMonth) return null
        return lunarHoliday(lunar)
    }

    private fun solarHoliday(d: SolarDate): String? =
        when (d.month) {
            // ── Ngày lễ chính thức Việt Nam (nghỉ có lương) ──────────────────
            1 if d.day == 1 -> "Tết Dương Lịch"
            4 if d.day == 30 -> "Giải Phóng Miền Nam"
            5 if d.day == 1 -> "Quốc Tế Lao Động"
            9 if d.day == 2 -> "Quốc Khánh"

            // ── Ngày kỷ niệm Việt Nam ─────────────────────────────────────────
            2 if d.day == 3 -> "Ngày Thành Lập Đảng"
            3 if d.day == 26 -> "Ngày Thành Lập Đoàn"
            5 if d.day == 19 -> "Sinh Nhật Bác Hồ"
            6 if d.day == 21 -> "Ngày Báo Chí VN"
            6 if d.day == 28 -> "Ngày Gia Đình VN"
            7 if d.day == 27 -> "Ngày Thương Binh LS"
            8 if d.day == 19 -> "Cách Mạng Tháng Tám"
            10 if d.day == 20 -> "Ngày Phụ Nữ VN"
            11 if d.day == 20 -> "Ngày Nhà Giáo VN"
            12 if d.day == 22 -> "Ngày Thành Lập QĐND"

            // ── Ngày lễ quốc tế phổ biến ─────────────────────────────────────
            2 if d.day == 14 -> "Lễ Tình Nhân"
            3 if d.day == 8 -> "Quốc Tế Phụ Nữ"
            4 if d.day == 22 -> "Ngày Trái Đất"
            6 if d.day == 1 -> "Quốc Tế Thiếu Nhi"
            10 if d.day == 31 -> "Halloween"
            12 if d.day == 24 -> "Đêm Noel"
            12 if d.day == 25 -> "Giáng Sinh"
            12 if d.day == 31 -> "Đêm Giao Thừa DL"
            else -> null
        }

    private fun lunarHoliday(l: LunarDate): String? =
        when (l.month) {
            // ── Tết Nguyên Đán ───────────────────────────────────────────────
            1 if l.day == 1 -> "Mùng 1 Tết"
            1 if l.day == 2 -> "Mùng 2 Tết"
            1 if l.day == 3 -> "Mùng 3 Tết"
            1 if l.day in 4..7 -> "Tết Nguyên Đán"

            // ── Các lễ tết, rằm quan trọng ────────────────────────────────────
            1 if l.day == 15 -> "Rằm Tháng Giêng"
            3 if l.day == 3 -> "Tết Hàn Thực"
            3 if l.day == 10 -> "Giỗ Tổ Hùng Vương"
            4 if l.day == 15 -> "Phật Đản"
            5 if l.day == 5 -> "Tết Đoan Ngọ"
            7 if l.day == 7 -> "Thất Tịch"
            7 if l.day == 15 -> "Lễ Vu Lan"
            8 if l.day == 15 -> "Tết Trung Thu"
            9 if l.day == 9 -> "Tết Trùng Cửu"
            10 if l.day == 15 -> "Tết Hạ Nguyên"
            12 if l.day == 23 -> "Ông Táo Về Trời"
            12 if l.day in 29..30 -> "Tất Niên"
            else -> null
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
