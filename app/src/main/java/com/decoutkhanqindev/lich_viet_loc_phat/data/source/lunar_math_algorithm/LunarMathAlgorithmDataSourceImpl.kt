package com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm

import com.decoutkhanqindev.lich_viet_loc_phat.data.source.LunarMath
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin

class LunarMathAlgorithmDataSourceImpl : LunarMathAlgorithmDataSource {

    override fun solarToLunar(solar: SolarDate): LunarDate {
        val r = convertSolar2Lunar(
            dd = solar.day,
            mm = solar.month,
            yyyy = solar.year,
            timeZone = VN_TZ
        )
        return LunarDate(r.day, r.month, r.year, r.leap == 1)
    }

    override fun lunarToSolar(lunar: LunarDate): SolarDate {
        val (d, m, y) = convertLunar2Solar(
            lunarDay = lunar.day,
            lunarMonth = lunar.month,
            lunarYear = lunar.year,
            lunarLeap = if (lunar.isLeapMonth) 1 else 0,
            timeZone = VN_TZ
        )
        if (d == 0) throw IllegalArgumentException(
            "Tháng nhuận ${lunar.month} không tồn tại trong năm Âm lịch ${lunar.year}"
        )
        return SolarDate(d, m, y)
    }

    override fun calculateCanChi(solar: SolarDate): CanChi =
        calculateCanChi(solar, solarToLunar(solar))

    override fun calculateCanChi(solar: SolarDate, lunar: LunarDate): CanChi {
        val jdn = jdFromDate(solar.day, solar.month, solar.year)
        val canNamIdx = (lunar.year + 6) % 10
        val chiNamIdx = (lunar.year + 8) % 12
        val canThangStart = CAN_THANG_START[canNamIdx % 5]
        val canThangIdx = (canThangStart + lunar.month - 1) % 10
        val chiThangIdx = (lunar.month + 1) % 12
        val canNgayIdx = (jdn + 9) % 10
        val chiNgayIdx = (jdn + 1) % 12
        return CanChi(
            canNam = CAN[canNamIdx],
            chiNam = CHI[chiNamIdx],
            canThang = CAN[canThangIdx],
            chiThang = CHI[chiThangIdx],
            canNgay = CAN[canNgayIdx],
            chiNgay = CHI[chiNgayIdx],
        )
    }

    override fun getAuspiciousHours(solar: SolarDate): List<HourInfo> {
        val jdn = jdFromDate(
            dd = solar.day,
            mm = solar.month,
            yyyy = solar.year
        )
        val chiNgayIdx = (jdn + 1) % 12
        val auspiciousSet = AUSPICIOUS_HOURS[chiNgayIdx % 6].toHashSet()
        return HOUR_NAMES.mapIndexed { i, (name, timeRange) ->
            HourInfo(name = name, timeRange = timeRange, isAuspicious = i in auspiciousSet)
        }
    }

    // -----------------------------------------------------------------------
    // Thuật toán Hồ Ngọc Đức (Kotlin port)
    // -----------------------------------------------------------------------

    private fun jdFromDate(dd: Int, mm: Int, yyyy: Int): Int = LunarMath.jdFromDate(dd, mm, yyyy)

    internal fun jdToDate(jd: Int): Triple<Int, Int, Int> {
        val a: Int
        val b: Int
        val c: Int
        if (jd > 2299160) {
            a = jd + 32044
            b = (4 * a + 3) / 146097
            c = a - (146097 * b) / 4
        } else {
            b = 0
            c = jd + 32082
        }
        val d = (4 * c + 3) / 1461
        val e = c - (1461 * d) / 4
        val m = (5 * e + 2) / 153
        val day = e - (153 * m + 2) / 5 + 1
        val month = m + 3 - 12 * (m / 10)
        val year = 100 * b + d - 4800 + m / 10
        return Triple(day, month, year)
    }

    private fun newMoon(k: Int): Double {
        val dr = PI / 180.0
        val T = k / 1236.85
        val T2 = T * T
        val T3 = T2 * T
        var jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3
        jd1 += 0.00033 * sin((166.56 + 132.87 * T - 0.009173 * T2) * dr)
        val M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3
        val Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3
        val F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3
        var c1 = (0.1734 - 0.000393 * T) * sin(M * dr) + 0.0021 * sin(2 * dr * M)
        c1 -= 0.4068 * sin(Mpr * dr) + 0.0161 * sin(dr * 2 * Mpr)
        c1 -= 0.0004 * sin(dr * 3 * Mpr)
        c1 += 0.0104 * sin(dr * 2 * F) - 0.0051 * sin(dr * (M + Mpr))
        c1 -= 0.0074 * sin(dr * (M - Mpr)) + 0.0004 * sin(dr * (2 * F + M))
        c1 -= 0.0004 * sin(dr * (2 * F - M)) - 0.0006 * sin(dr * (2 * F + Mpr))
        c1 += 0.0010 * sin(dr * (2 * F - Mpr)) + 0.0005 * sin(dr * (2 * Mpr + M))
        val deltat = if (T < -11) {
            0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3
        } else {
            -0.000278 + 0.000265 * T + 0.000262 * T2
        }
        return jd1 + c1 - deltat
    }

    private fun sunLongitudeDeg(jdn: Double): Double = LunarMath.sunLongitudeDeg(jdn)

    private fun getNewMoonDay(k: Int, timeZone: Double): Int =
        floor(newMoon(k) + 0.5 + timeZone / 24.0).toInt()

    // Returns zodiac zone index 0-11 (30° per zone)
    private fun getSunLongitudeZone(dayNumber: Int, timeZone: Double): Int =
        floor(sunLongitudeDeg(dayNumber - 0.5 - timeZone / 24.0) / 30.0).toInt()

    private fun getLunarMonth11(yyyy: Int, timeZone: Double): Int {
        val off = jdFromDate(31, 12, yyyy) - 2415021
        val k = floor(off / 29.530588853).toInt()
        var nm = getNewMoonDay(k, timeZone)
        if (getSunLongitudeZone(nm, timeZone) >= 9) nm = getNewMoonDay(k - 1, timeZone)
        return nm
    }

    private fun getLeapMonthOffset(a11: Int, timeZone: Double): Int {
        val k = floor((a11 - 2415021.076998695) / 29.530588853 + 0.5).toInt()
        var last = 0
        var i = 1
        var arc = getSunLongitudeZone(getNewMoonDay(k + i, timeZone), timeZone)
        while (arc != last) {
            last = arc
            i++
            arc = getSunLongitudeZone(getNewMoonDay(k + i, timeZone), timeZone)
            if (i >= 14) break
        }
        return i - 1
    }

    private data class LunarResult(val day: Int, val month: Int, val year: Int, val leap: Int)

    private fun convertSolar2Lunar(dd: Int, mm: Int, yyyy: Int, timeZone: Double): LunarResult {
        val dayNumber = jdFromDate(dd, mm, yyyy)
        val k = floor((dayNumber - 2415021.076998695) / 29.530588853).toInt()
        var monthStart = getNewMoonDay(k + 1, timeZone)
        if (monthStart > dayNumber) monthStart = getNewMoonDay(k, timeZone)
        var a11 = getLunarMonth11(yyyy, timeZone)
        var b11 = a11
        val lunarYear: Int
        if (a11 >= monthStart) {
            lunarYear = yyyy
            a11 = getLunarMonth11(yyyy - 1, timeZone)
        } else {
            lunarYear = yyyy + 1
            b11 = getLunarMonth11(yyyy + 1, timeZone)
        }
        val lunarDay = dayNumber - monthStart + 1
        val diff = floor((monthStart - a11).toDouble() / 29.0).toInt()
        var lunarLeap = 0
        var lunarMonth = diff + 11
        if (b11 - a11 > 365) {
            val leapMonthDiff = getLeapMonthOffset(a11, timeZone)
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10
                if (diff == leapMonthDiff) lunarLeap = 1
            }
        }
        if (lunarMonth > 12) lunarMonth -= 12
        return if (lunarMonth >= 11 && diff < 4) {
            LunarResult(lunarDay, lunarMonth, lunarYear - 1, lunarLeap)
        } else {
            LunarResult(lunarDay, lunarMonth, lunarYear, lunarLeap)
        }
    }

    private fun convertLunar2Solar(
        lunarDay: Int, lunarMonth: Int, lunarYear: Int, lunarLeap: Int, timeZone: Double,
    ): Triple<Int, Int, Int> {
        val a11: Int
        val b11: Int
        if (lunarMonth < 11) {
            a11 = getLunarMonth11(lunarYear - 1, timeZone)
            b11 = getLunarMonth11(lunarYear, timeZone)
        } else {
            a11 = getLunarMonth11(lunarYear, timeZone)
            b11 = getLunarMonth11(lunarYear + 1, timeZone)
        }
        val k = floor(0.5 + (a11 - 2415021.076998695) / 29.530588853).toInt()
        var off = lunarMonth - 11
        if (off < 0) off += 12
        if (b11 - a11 > 365) {
            val leapOff = getLeapMonthOffset(a11, timeZone)
            var leapMonth = leapOff - 2
            if (leapMonth < 0) leapMonth += 12
            if (lunarLeap != 0 && lunarMonth != leapMonth) return Triple(0, 0, 0)
            if (lunarLeap != 0 || off >= leapOff) off++
        }
        val monthStart = getNewMoonDay(k + off, timeZone)
        return jdToDate(monthStart + lunarDay - 1)
    }

    companion object {
        private const val VN_TZ = 7.0

        private val CAN by lazy {
            listOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")
        }
        private val CHI by lazy {
            listOf(
                "Tý",
                "Sửu",
                "Dần",
                "Mão",
                "Thìn",
                "Tỵ",
                "Ngọ",
                "Mùi",
                "Thân",
                "Dậu",
                "Tuất",
                "Hợi"
            )
        }

        // Starting Can index for lunar month 1, indexed by (canNam % 5)
        // Giáp/Kỷ→Bính(2), Ất/Canh→Mậu(4), Bính/Tân→Canh(6), Đinh/Nhâm→Nhâm(8), Mậu/Quý→Giáp(0)
        private val CAN_THANG_START by lazy { intArrayOf(2, 4, 6, 8, 0) }

        // Auspicious hour indices (0-11) per day Chi group (chiNgayIdx % 6)
        // Group: Tý/Ngọ, Sửu/Mùi, Dần/Thân, Mão/Dậu, Thìn/Tuất, Tỵ/Hợi
        private val AUSPICIOUS_HOURS by lazy {
            arrayOf(
                intArrayOf(0, 1, 3, 6, 8, 9),    // Tý(0), Ngọ(6)
                intArrayOf(2, 3, 5, 8, 10, 11),   // Sửu(1), Mùi(7)
                intArrayOf(0, 1, 4, 7, 9, 10),    // Dần(2), Thân(8)
                intArrayOf(0, 2, 3, 6, 9, 11),    // Mão(3), Dậu(9)
                intArrayOf(0, 1, 4, 5, 8, 11),    // Thìn(4), Tuất(10)
                intArrayOf(0, 2, 3, 5, 6, 9),     // Tỵ(5), Hợi(11)
            )
        }

        // 12 two-hour slots starting from Tý (23:00-01:00)
        private val HOUR_NAMES by lazy {
            listOf(
                "Tý" to "23:00 - 01:00",
                "Sửu" to "01:00 - 03:00",
                "Dần" to "03:00 - 05:00",
                "Mão" to "05:00 - 07:00",
                "Thìn" to "07:00 - 09:00",
                "Tỵ" to "09:00 - 11:00",
                "Ngọ" to "11:00 - 13:00",
                "Mùi" to "13:00 - 15:00",
                "Thân" to "15:00 - 17:00",
                "Dậu" to "17:00 - 19:00",
                "Tuất" to "19:00 - 21:00",
                "Hợi" to "21:00 - 23:00",
            )
        }
    }
}
