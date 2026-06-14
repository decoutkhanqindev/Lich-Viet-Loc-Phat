package com.decoutkhanqindev.lich_viet_loc_phat.presentation.model

object CalendarProperties {
    val months by lazy {
        listOf(
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",
        )
    }

    val weekdaysMonFirst by lazy {
        listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
    }

    val weekdaysSunFirst by lazy {
        listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7")
    }

    const val PICKER_YEAR_MIN = 1900
    const val PICKER_YEAR_MAX = 2100
    val pickerYears by lazy {
        (PICKER_YEAR_MIN..PICKER_YEAR_MAX).map { it.toString() }
    }
}
