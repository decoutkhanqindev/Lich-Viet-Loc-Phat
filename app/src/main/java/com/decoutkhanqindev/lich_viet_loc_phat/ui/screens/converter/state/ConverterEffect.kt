package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state

sealed interface ConverterEffect {
    data object ScrollResultIntoView : ConverterEffect
}