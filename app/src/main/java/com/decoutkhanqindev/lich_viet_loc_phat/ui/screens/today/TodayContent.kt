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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.CanChi
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.LunarDate
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.theme.AuspiciousGold
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BaTrauDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.HolidayDot
import com.decoutkhanqindev.lich_viet_loc_phat.theme.InauspiciousGray
import com.decoutkhanqindev.lich_viet_loc_phat.theme.IvoryWhite
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauToi
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SolarTermColor
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.GlassCard
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.onClick
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
            .background(Brush.verticalGradient(listOf(BaTrauDark, NauToi))),
    ) {
        val contentKey = when {
            state.isLoading -> "loading"
            state.error != null -> "error"
            else -> "content"
        }

        AnimatedContent(
            targetState = contentKey,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith
                        fadeOut(tween(180))
            },
            label = "TodayContentTransition",
        ) { key ->
            when (key) {
                "loading" -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldAccent)
                }

                "error" -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error ?: "",
                        color = IvoryWhite,
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
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
                                onPrev = { onIntent(TodayIntent.NavigateToPrevDay) },
                                onNext = { onIntent(TodayIntent.NavigateToNextDay) },
                                onToday = { onIntent(TodayIntent.RequestToday) },
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
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onToday: () -> Unit,
) {
    val weekdays = remember { listOf("CN", "T2", "T3", "T4", "T5", "T6", "T7") }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
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
                Icon(
                    Icons.Default.Today,
                    contentDescription = "Hôm nay",
                    tint = GoldAccent,
                    modifier = Modifier
                        .onClick { onToday() }
                        .size(28.dp)
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Default.ChevronLeft,
                    contentDescription = "Ngày trước",
                    tint = IvoryWhite,
                    modifier = Modifier
                        .onClick { onPrev() }
                        .size(32.dp)
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Ngày sau",
                    tint = IvoryWhite,
                    modifier = Modifier
                        .onClick { onNext() }
                        .size(32.dp)
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
                            "Dương Lịch",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${displaySolar.day}",
                            color = IvoryWhite,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 60.sp
                        )
                        Text(
                            "Tháng ${displaySolar.month} · ${displaySolar.year}",
                            color = IvoryWhite.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                        Text(
                            weekdays[dayOfWeek],
                            color = GoldAccent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(90.dp)
                            .width(1.dp)
                            .background(GlassBorder),
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            "Âm Lịch",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "${lunar.day}",
                            color = IvoryWhite,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 60.sp
                        )
                        val leapNote = if (lunar.isLeapMonth) " (Nhuận)" else ""
                        Text(
                            "Tháng ${lunar.month}$leapNote · ${lunar.year}",
                            color = IvoryWhite.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = holiday != null || solarTerm != null,
                enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 2 },
                exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { it / 2 },
            ) {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (holiday != null) {
                        HolidayBadge(label = holiday)
                    }
                    if (holiday != null && solarTerm != null) {
                        Spacer(Modifier.width(8.dp))
                    }
                    if (solarTerm != null) {
                        SolarTermBadge(label = solarTerm)
                    }
                }
            }
        }
    }
}

@Composable
private fun HolidayBadge(label: String) {
    val shape = remember { RoundedCornerShape(20.dp) }
    Box(
        modifier = Modifier
            .background(HolidayDot.copy(alpha = 0.15f), shape)
            .border(1.dp, HolidayDot.copy(alpha = 0.5f), shape)
            .padding(horizontal = 14.dp, vertical = 5.dp),
    ) {
        Text(
            label,
            color = HolidayDot,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun SolarTermBadge(label: String) {
    val shape = remember { RoundedCornerShape(20.dp) }
    Box(
        modifier = Modifier
            .background(SolarTermColor.copy(alpha = 0.12f), shape)
            .border(1.dp, SolarTermColor.copy(alpha = 0.45f), shape)
            .padding(horizontal = 14.dp, vertical = 5.dp),
    ) {
        Text(
            label,
            color = SolarTermColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun CanChiCard(
    canChi: CanChi,
    solarTerm: String?,
) {
    GlassCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Can Chi", color = GoldAccent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            CanChiRow("Năm", can = canChi.canNam, chi = canChi.chiNam)
            CanChiRow("Tháng", can = canChi.canThang, chi = canChi.chiThang)
            CanChiRow("Ngày", can = canChi.canNgay, chi = canChi.chiNgay)
            AnimatedVisibility(
                visible = solarTerm != null,
                enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 2 },
                exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { it / 2 },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tiết khí: ", color = IvoryWhite.copy(alpha = 0.6f), fontSize = 13.sp)
                    solarTerm?.let {
                        Text(
                            it,
                            color = IvoryWhite,
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
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            "$label:",
            color = IvoryWhite.copy(alpha = 0.55f),
            fontSize = 13.sp,
            modifier = Modifier.width(52.dp),
        )
        Text("$can $chi", color = IvoryWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AuspiciousHoursCard(hours: ImmutableList<HourInfoUiModel>) {
    GlassCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Giờ Hoàng Đạo / Hắc Đạo",
                color = GoldAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
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
        if (hour.isAuspicious) AuspiciousGold.copy(alpha = 0.18f) else Color.Transparent
    }
    val textColor = remember(hour.isAuspicious) {
        if (hour.isAuspicious) AuspiciousGold else InauspiciousGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        Column {
            Text(
                hour.name,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = if (hour.isAuspicious) FontWeight.SemiBold else FontWeight.Normal,
            )
            Text(hour.timeRange, color = textColor.copy(alpha = 0.75f), fontSize = 9.sp)
        }
    }
}