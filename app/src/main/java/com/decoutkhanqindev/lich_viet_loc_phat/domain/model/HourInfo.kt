package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

// PRD: liệt kê 12 khung giờ, tên con giáp và khoảng thời gian Dương lịch tương ứng
data class HourInfo(
    val name: String,         // Tên con giáp: Tý, Sửu, Dần, Mão, ...
    val timeRange: String,    // Khoảng giờ dương lịch: "23:00 - 01:00"
    val isAuspicious: Boolean, // true = Hoàng Đạo, false = Hắc Đạo
)
