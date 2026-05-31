package com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

interface StaticAssetDataSource {
    fun getSolarTerm(solar: SolarDate): String?
    fun getHoliday(solar: SolarDate, lunar: LunarDate): String?
}
