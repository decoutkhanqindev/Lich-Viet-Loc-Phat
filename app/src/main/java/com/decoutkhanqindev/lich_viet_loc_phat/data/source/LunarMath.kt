package com.decoutkhanqindev.lich_viet_loc_phat.data.source

import kotlin.math.PI
import kotlin.math.floor
import kotlin.math.sin

internal object LunarMath {

    internal fun jdFromDate(dd: Int, mm: Int, yyyy: Int): Int {
        val a = (14 - mm) / 12
        val y = yyyy + 4800 - a
        val m = mm + 12 * a - 3
        var jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
        if (jd < 2299161) jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083
        return jd
    }

    internal fun sunLongitudeDeg(jdn: Double): Double {
        val dr = PI / 180.0
        val T = (jdn - 2451545.0) / 36525.0
        val T2 = T * T
        val M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2
        val L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2
        var dl = (1.9146 - 0.004817 * T - 0.000014 * T2) * sin(dr * M)
        dl += (0.019993 - 0.000101 * T) * sin(dr * 2 * M) + 0.00029 * sin(dr * 3 * M)
        var L = L0 + dl
        L -= 360.0 * floor(L / 360.0)
        if (L < 0) L += 360.0
        return L
    }
}
