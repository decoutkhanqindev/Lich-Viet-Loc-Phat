package com.decoutkhanqindev.lich_viet_loc_phat.data.repository

import com.decoutkhanqindev.lich_viet_loc_phat.data.source.lunar_math_algorithm.LunarMathAlgorithmDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.data.source.static_asset.StaticAssetDataSource
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DailyMetadata
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DayCell
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CalendarRepositoryImpl(
    private val algorithmSource: LunarMathAlgorithmDataSource,
    private val assetSource: StaticAssetDataSource,
) : CalendarRepository {

    override suspend fun getDailyMetadata(date: SolarDate): DailyMetadata =
        withContext(Dispatchers.Default) {
            val lunar = algorithmSource.solarToLunar(date)
            val canChi = algorithmSource.calculateCanChi(date, lunar)
            val hours = algorithmSource.getAuspiciousHours(date)
            val term = assetSource.getSolarTerm(date)
            val holiday = assetSource.getHoliday(date, lunar)
            DailyMetadata(date, lunar, canChi, hours, term, holiday)
        }

    override suspend fun getDaysInMonth(year: Int, month: Int): List<DayCell> =
        withContext(Dispatchers.Default) {
            val today = SolarDate.today()
            val firstDay = LocalDate.of(year, month, 1)
            // ISO: Monday=1…Sunday=7. Grid starts on Monday, so offset = dayOfWeek.value - 1
            val startOffset = firstDay.dayOfWeek.value - 1
            val startDate = firstDay.minusDays(startOffset.toLong())

            (0 until 42).map { i ->
                val date = startDate.plusDays(i.toLong())
                val solar = SolarDate(date.dayOfMonth, date.monthValue, date.year)
                val lunar = algorithmSource.solarToLunar(solar)
                DayCell(
                    solar = solar,
                    lunar = lunar,
                    isCurrentMonth = date.monthValue == month,
                    isToday = solar == today,
                    holiday = assetSource.getHoliday(solar, lunar),
                    canChi = algorithmSource.calculateCanChi(solar, lunar),
                    solarTerm = assetSource.getSolarTerm(solar),
                )
            }
        }

    override suspend fun convertSolarToLunar(solar: SolarDate): LunarDate =
        withContext(Dispatchers.Default) { algorithmSource.solarToLunar(solar) }

    override suspend fun convertLunarToSolar(lunar: LunarDate): SolarDate =
        withContext(Dispatchers.Default) { algorithmSource.lunarToSolar(lunar) }

    override suspend fun calculateCanChi(date: SolarDate): CanChi =
        withContext(Dispatchers.Default) { algorithmSource.calculateCanChi(date) }

    override suspend fun getAuspiciousHours(date: SolarDate): List<HourInfo> =
        withContext(Dispatchers.Default) { algorithmSource.getAuspiciousHours(date) }

    override suspend fun getSolarTerm(date: SolarDate): String? =
        withContext(Dispatchers.Default) { assetSource.getSolarTerm(date) }
}
