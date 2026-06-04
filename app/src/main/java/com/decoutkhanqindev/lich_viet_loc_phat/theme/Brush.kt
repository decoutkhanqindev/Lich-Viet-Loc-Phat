package com.decoutkhanqindev.lich_viet_loc_phat.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val GiayDoBrush = Brush.verticalGradient(listOf(GiayDo, GiayDoMid, GiayDoDark))

val DateSeparatorBrush = Brush.verticalGradient(
    listOf(Color.Transparent, BorderStrong, BorderStrong, Color.Transparent)
)

val TodayCellBrush = Brush.verticalGradient(listOf(DoSonLightCell, DoSonCell))

val PickerSeparatorBrush = Brush.verticalGradient(
    listOf(Color.Transparent, BorderWarmFaded, BorderWarmFaded, Color.Transparent)
)

val PickerFadeTopBrush = Brush.verticalGradient(listOf(SurfaceCard, Color.Transparent))
val PickerFadeBottomBrush = Brush.verticalGradient(listOf(Color.Transparent, SurfaceCard))
