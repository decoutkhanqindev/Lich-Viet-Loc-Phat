package com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

interface LunarMathAlgorithmDataSource {
    fun solarToLunar(solar: SolarDate): LunarDate
    fun lunarToSolar(lunar: LunarDate): SolarDate
    fun calculateCanChi(solar: SolarDate): CanChi
    fun calculateCanChi(solar: SolarDate, lunar: LunarDate): CanChi
    fun getAuspiciousHours(solar: SolarDate): List<HourInfo>
}
