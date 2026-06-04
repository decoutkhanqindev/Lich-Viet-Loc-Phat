package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DateSeparatorBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoLe
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoLeSubtle
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoleBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmFaded
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmMuted
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmSoft
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBich
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBichBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBichHint
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBichLightBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBichMuted
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBichSubtle
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongDim
import com.decoutkhanqindev.lich_viet_loc_phat.theme.XamMo
import com.decoutkhanqindev.lich_viet_loc_phat.theme.XamMoMuted
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape20dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape6dp
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.AppCard
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.PrevNextButtons
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.TodayButton
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.AnimationContentKey
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.CalendarProperties
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.HourInfoUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state.TodayIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today.state.TodayState
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate

@Composable
fun TodayContent(
    state: TodayState,
    onIntent: (TodayIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush),
    ) {
        val contentKey = when {
            state.isLoading -> AnimationContentKey.Loading
            state.error != null -> AnimationContentKey.Error
            else -> AnimationContentKey.Content
        }

        AnimatedContent(
            targetState = contentKey,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(180))
            },
            label = "TodayContentTransition",
        ) { key ->
            when (key) {
                AnimationContentKey.Loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VangDong)
                }

                AnimationContentKey.Error -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "",
                        color = MucDen,
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                AnimationContentKey.Content -> {
                    if (state.dailyMetadata != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            DateNavigationHeader(
                                solar = state.dailyMetadata.solar,
                                lunar = state.dailyMetadata.lunar,
                                holiday = state.dailyMetadata.holiday,
                                solarTerm = state.dailyMetadata.solarTerm,
                                showTodayButton = state.showTodayButton,
                                onPrev = {
                                    onIntent(TodayIntent.NavigateToPrevDay)
                                },
                                onNext = {
                                    onIntent(TodayIntent.NavigateToNextDay)
                                },
                                onToday = {
                                    onIntent(TodayIntent.RequestToday)
                                },
                            )
                            CanChiCard(
                                canChi = state.dailyMetadata.canChi,
                                solarTerm = state.dailyMetadata.solarTerm,
                            )
                            AuspiciousHoursCard(hours = state.dailyMetadata.auspiciousHours)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateNavigationHeader(
    solar: SolarDate,
    lunar: LunarDate,
    holiday: String?,
    solarTerm: String?,
    showTodayButton: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
) {
    val weekdays = remember { CalendarProperties.weekdaysSunFirst }

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TodayButton(
                    visible = showTodayButton,
                    onClick = onToday
                )
                Spacer(Modifier.weight(1f))
                PrevNextButtons(
                    onPrev = onPrev,
                    onNext = onNext
                )
            }
            Spacer(Modifier.height(8.dp))
            AnimatedContent(
                targetState = solar,
                transitionSpec = {
                    (slideInVertically { it / 3 } + fadeIn(tween(200))) togetherWith
                            (slideOutVertically { -it / 3 } + fadeOut(tween(150)))
                },
                label = "DateTransition",
            ) { displaySolar ->
                val dayOfWeek = remember(
                    displaySolar.year,
                    displaySolar.month,
                    displaySolar.day
                ) {
                    LocalDate.of(
                        displaySolar.year,
                        displaySolar.month,
                        displaySolar.day
                    ).dayOfWeek.value % 7
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            "DƯƠNG LỊCH",
                            color = VangDongDim,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp,
                        )
                        Text(
                            "${displaySolar.day}",
                            color = MucDen,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Light,
                            lineHeight = 68.sp,
                        )
                        Text(
                            "Tháng ${displaySolar.month} · ${displaySolar.year}",
                            color = NauAm,
                            fontSize = 13.sp,
                        )
                        Text(
                            weekdays[dayOfWeek],
                            color = VangDong,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(96.dp)
                            .width(1.dp)
                            .background(DateSeparatorBrush),
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            "ÂM LỊCH",
                            color = VangDongDim,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp,
                        )
                        Text(
                            "${lunar.day}",
                            color = NauAm,
                            fontSize = 58.sp,
                            fontWeight = FontWeight.ExtraLight,
                            lineHeight = 62.sp,
                        )
                        val leapNote = remember(lunar.isLeapMonth) {
                            if (lunar.isLeapMonth) " (Nhuận)" else ""
                        }
                        Text(
                            "Tháng ${lunar.month}$leapNote · ${lunar.year}",
                            color = NauAmFaded,
                            fontSize = 13.sp,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = holiday != null || solarTerm != null,
                enter = fadeIn(tween(220))
                        + slideInVertically(tween(220)) { it / 2 },
                exit = fadeOut(tween(180))
                        + slideOutVertically(tween(180)) { it / 2 },
            ) {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (holiday != null) HolidayBadge(label = holiday)
                    if (holiday != null && solarTerm != null) Spacer(Modifier.width(8.dp))
                    if (solarTerm != null) SolarTermBadge(label = solarTerm)
                }
            }
        }
    }
}

@Composable
private fun HolidayBadge(label: String) {
    Box(
        modifier = Modifier
            .background(color = DoLeSubtle, shape = roundedCornerShape20dp)
            .border(
                width = 1.dp,
                color = DoleBorder,
                shape = roundedCornerShape20dp
            )
            .padding(horizontal = 14.dp, vertical = 5.dp),
    ) {
        Text(
            label,
            color = DoLe,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SolarTermBadge(label: String) {
    Box(
        modifier = Modifier
            .background(color = NgocBichSubtle, shape = roundedCornerShape20dp)
            .border(
                width = 1.dp,
                color = NgocBichBorder,
                shape = roundedCornerShape20dp
            )
            .padding(horizontal = 14.dp, vertical = 5.dp),
    ) {
        Text(
            label,
            color = NgocBich,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CanChiCard(
    canChi: CanChi,
    solarTerm: String?,
) {
    AppCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                "CAN CHI",
                color = VangDong,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )
            Spacer(Modifier.height(2.dp))
            CanChiRow(
                "Năm",
                can = canChi.canNam,
                chi = canChi.chiNam
            )
            CanChiRow(
                "Tháng",
                can = canChi.canThang,
                chi = canChi.chiThang
            )
            CanChiRow(
                "Ngày",
                can = canChi.canNgay,
                chi = canChi.chiNgay
            )
            AnimatedVisibility(
                visible = solarTerm != null,
                enter = fadeIn(tween(220))
                        + slideInVertically(tween(220)) { it / 2 },
                exit = fadeOut(tween(180))
                        + slideOutVertically(tween(180)) { it / 2 },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Tiết khí: ",
                        color = NauAmMuted,
                        fontSize = 13.sp
                    )
                    solarTerm?.let {
                        Text(
                            it,
                            color = NgocBich,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CanChiRow(
    label: String,
    can: String,
    chi: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$label:",
            color = NauAmSoft,
            fontSize = 13.sp,
            modifier = Modifier.width(52.dp),
        )
        Text(
            "$can $chi",
            color = MucDen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AuspiciousHoursCard(hours: ImmutableList<HourInfoUiModel>) {
    AppCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "GIỜ HOÀNG ĐẠO / HẮC ĐẠO",
                color = VangDong,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )
            val columns = remember(hours) { hours.chunked(6) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                columns.forEach { col ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        col.forEach { HourChip(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun HourChip(hour: HourInfoUiModel) {
    val bgColor = remember(hour.isAuspicious) {
        if (hour.isAuspicious) NgocBichHint else Color.Transparent
    }
    val borderColor = remember(hour.isAuspicious) {
        if (hour.isAuspicious) NgocBichLightBorder else Color.Transparent
    }
    val textColor = remember(hour.isAuspicious) {
        if (hour.isAuspicious) NgocBich else XamMo
    }
    val textColorMuted = remember(hour.isAuspicious) {
        if (hour.isAuspicious) NgocBichMuted else XamMoMuted
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = bgColor, shape = roundedCornerShape6dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = roundedCornerShape6dp
            )
            .padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        Column {
            Text(
                hour.name,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = if (hour.isAuspicious) FontWeight.SemiBold else FontWeight.Normal,
            )
            Text(
                hour.timeRange,
                color = textColorMuted,
                fontSize = 9.sp
            )
        }
    }
}
