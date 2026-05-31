package com.decoutkhanqindev.lich_viet_loc_phat.domain.model

// PRD yêu cầu hiển thị Can-Chi của Ngày, Tháng và Năm
data class CanChi(
    val canNam: String,
    val chiNam: String,
    val canThang: String,
    val chiThang: String,
    val canNgay: String,
    val chiNgay: String,
)
