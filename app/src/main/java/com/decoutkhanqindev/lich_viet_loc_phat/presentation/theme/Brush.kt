package com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val GiayDoBrush = Brush.verticalGradient(listOf(GiayDo, GiayDoMid, GiayDoDark))

val DateSeparatorBrush = Brush.verticalGradient(
    listOf(Color.Transparent, BorderStrong, BorderStrong, Color.Transparent)
)

val TodayCellBrush = Brush.verticalGradient(listOf(DoSonLightAlpha95, DoSonAlpha90))

val PickerSeparatorBrush = Brush.verticalGradient(
    listOf(Color.Transparent, BorderWarmAlpha50, BorderWarmAlpha50, Color.Transparent)
)

val PickerFadeTopBrush = Brush.verticalGradient(listOf(SurfaceCard, Color.Transparent))
val PickerFadeBottomBrush = Brush.verticalGradient(listOf(Color.Transparent, SurfaceCard))
