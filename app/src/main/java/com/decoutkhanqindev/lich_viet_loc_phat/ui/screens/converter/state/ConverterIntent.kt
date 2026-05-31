package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state

import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode

sealed interface ConverterIntent {
    data class ChangeMode(val mode: ConvertMode) : ConverterIntent
    data class InputChanged(
        val day: Int,
        val month: Int,
        val year: Int,
    ) : ConverterIntent

    data class ToggleLeapMonth(val checked: Boolean) : ConverterIntent
    data object Convert : ConverterIntent
}