package com.decoutkhanqindev.lich_viet_loc_phat.domain.repository

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DailyMetadata
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DayCell
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.HourInfo
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate

interface CalendarRepository {

    // Lấy toàn bộ metadata cho một ngày: Âm lịch, Can-Chi, Giờ H.Đạo/H.Đạo, Tiết khí
    suspend fun getDailyMetadata(date: SolarDate): DailyMetadata

    // Lấy 42 ô (6 hàng × 7 cột) cho lưới lịch tháng, bao gồm ngày tràn của tháng trước/sau
    suspend fun getDaysInMonth(year: Int, month: Int): List<DayCell>

    // Chuyển đổi Dương → Âm
    suspend fun convertSolarToLunar(solar: SolarDate): LunarDate

    // Chuyển đổi Âm → Dương (isLeapMonth trong LunarDate xử lý tháng nhuận)
    suspend fun convertLunarToSolar(lunar: LunarDate): SolarDate

    // Tính Can-Chi (Ngày, Tháng, Năm) cho một ngày Dương lịch
    suspend fun calculateCanChi(date: SolarDate): CanChi

    // Lấy 12 khung giờ Hoàng Đạo/Hắc Đạo của ngày
    suspend fun getAuspiciousHours(date: SolarDate): List<HourInfo>

    // Lấy tên Tiết khí nếu ngày đó có Tiết khí, null nếu không
    suspend fun getSolarTerm(date: SolarDate): String?
}
