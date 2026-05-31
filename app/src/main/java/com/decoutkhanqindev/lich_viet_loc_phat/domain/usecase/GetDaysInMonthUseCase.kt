package com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.DayCell
import com.decoutkhanqindev.lich_viet_loc_phat.domain.repository.CalendarRepository

class GetDaysInMonthUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(year: Int, month: Int): Result<List<DayCell>> =
        runCatching { repository.getDaysInMonth(year, month) }
}
