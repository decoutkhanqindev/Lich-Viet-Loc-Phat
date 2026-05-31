package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

// Đại diện cho một ô ngày trong lưới lịch tháng (Tab 2)
data class DayCell(
    val solar: SolarDate,
    val lunar: LunarDate,
    // Dùng để render ô tràn (overflow days) mờ hơn các ngày thuộc tháng hiện tại
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    // Tên ngày lễ quốc gia nếu có (PRD AC 2.5.2), null nếu không phải ngày lễ
    val holiday: String?,
)
