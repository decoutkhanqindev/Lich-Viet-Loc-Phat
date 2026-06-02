package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.CuoiTuan
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoLe
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoSon
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoSonLight
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDo
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoMid
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBich
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.PrevNextButtons
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.TodayButton
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.onClick
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DayCellUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarState
import kotlinx.collections.immutable.ImmutableList


@Composable
fun CalendarContent(
    state: CalendarState,
    onIntent: (CalendarIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GiayDo, GiayDoMid, GiayDoDark))),
    ) {
        CalendarMonthHeader(
            displayedMonth = state.displayedMonth,
            displayedYear = state.displayedYear,
            lunarYearLabel = state.lunarYearLabel,
            lunarMonthLabel = state.lunarMonthLabel,
            showTodayButton = state.showTodayButton,
            onToday = { onIntent(CalendarIntent.RequestToday) },
            onPrev = { onIntent(CalendarIntent.PrevMonth) },
            onNext = { onIntent(CalendarIntent.NextMonth) },
        )

        val weekdays = remember { listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 4.dp)
        ) {
            weekdays.forEachIndexed { idx, label ->
                Text(
                    label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = if (idx >= 5) CuoiTuan else NauAm,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(1.dp, BorderWarm, RoundedCornerShape(12.dp))
                .background(SurfaceCard, RoundedCornerShape(12.dp))
                .padding(8.dp),
        ) {
            CalendarGrid(
                isLoading = state.isLoading,
                error = state.error,
                days = state.days,
                showCanChiOnCell = state.showCanChiOnCell,
                onDayClick = { onIntent(CalendarIntent.SelectDay(it)) },
            )
        }
    }
}

@Immutable
private data class CalendarHeaderState(
    val month: Int,
    val year: Int,
    val lunarYear: String?,
    val lunarMonth: String?,
)

@Composable
private fun CalendarMonthHeader(
    displayedMonth: Int,
    displayedYear: Int,
    lunarYearLabel: String?,
    lunarMonthLabel: String?,
    showTodayButton: Boolean,
    onToday: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    val monthNames = remember {
        listOf(
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12",
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TodayButton(
            visible = showTodayButton,
            onClick = onToday,
            modifier = Modifier.weight(0.15f)
        )

        AnimatedContent(
            targetState = CalendarHeaderState(
                displayedMonth,
                displayedYear,
                lunarYearLabel,
                lunarMonthLabel
            ),
            modifier = Modifier.weight(0.7f),
            transitionSpec = {
                (slideInVertically { it / 3 } + fadeIn(tween(200))) togetherWith
                        (slideOutVertically { -it / 3 } + fadeOut(tween(150)))
            },
            label = "MonthYearTransition",
        ) { hs ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${monthNames[hs.month - 1]} · ${hs.year}",
                    textAlign = TextAlign.Center,
                    color = MucDen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                if (hs.lunarYear != null && hs.lunarMonth != null) {
                    Text(
                        "Năm ${hs.lunarYear} · Tháng ${hs.lunarMonth}",
                        textAlign = TextAlign.Center,
                        color = VangDong.copy(alpha = 0.75f),
                        fontSize = 11.sp,
                        letterSpacing = 0.3.sp,
                    )
                }
            }
        }

        PrevNextButtons(onPrev = onPrev, onNext = onNext)
    }
}

@Composable
private fun CalendarGrid(
    isLoading: Boolean,
    error: String?,
    days: ImmutableList<DayCellUiModel>,
    showCanChiOnCell: Boolean,
    onDayClick: (SolarDate) -> Unit,
) {
    val contentKey = when {
        isLoading -> "loading"
        error != null -> "error"
        else -> "content"
    }
    AnimatedContent(
        targetState = contentKey,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(180)) },
        label = "CalendarGridTransition",
    ) { key ->
        when (key) {
            "loading" -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp),
                    color = VangDong
                )
            }

            "error" -> Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "",
                    color = MucDen,
                    modifier = Modifier.padding(32.dp),
                    textAlign = TextAlign.Center,
                )
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                items(
                    items = days,
                    key = { "${it.solar.year}-${it.solar.month}-${it.solar.day}" },
                ) { cell ->
                    DayCell(
                        cell = cell,
                        showCanChi = showCanChiOnCell,
                        onClick = { onDayClick(cell.solar) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    cell: DayCellUiModel,
    showCanChi: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cellShape = remember { RoundedCornerShape(10.dp) }
    val todayOnRed = Color.White

    val solarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> MucDen.copy(alpha = 0.25f)
            cell.isToday -> todayOnRed
            else -> MucDen
        }
    }
    val lunarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> NauNhat.copy(alpha = 0.4f)
            cell.isToday -> todayOnRed.copy(alpha = 0.85f)
            else -> NauAm.copy(alpha = 0.7f)
        }
    }
    val canChiTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> VangDong.copy(alpha = 0.2f)
            cell.isToday -> todayOnRed.copy(alpha = 0.8f)
            else -> VangDong.copy(alpha = 0.7f)
        }
    }
    val holidayTextColor = remember(cell.isToday) {
        if (cell.isToday) todayOnRed else DoLe
    }
    val solarTermTextColor = remember(cell.isToday) {
        if (cell.isToday) todayOnRed else NgocBich
    }
    val lunarDotColor = remember(cell.isToday) {
        if (cell.isToday) todayOnRed.copy(alpha = 0.7f) else VangDong.copy(alpha = 0.7f)
    }

    Box(
        modifier = modifier
            .onClick(cellShape) { onClick() }
            .clip(cellShape)
            .then(
                when {
                    cell.isToday -> Modifier.background(
                        Brush.verticalGradient(
                            listOf(
                                DoSonLight.copy(alpha = 0.95f),
                                DoSon.copy(alpha = 0.90f)
                            )
                        )
                    )

                    cell.isSelected -> Modifier.border(
                        1.5.dp,
                        VangDong.copy(alpha = 0.5f),
                        cellShape
                    )

                    else -> Modifier
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${cell.solar.day}",
                color = solarTextColor,
                fontSize = 15.sp,
                fontWeight = if (cell.isToday) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            Text(
                text = if (cell.lunar.day == 1) "1/${cell.lunar.month}" else "${cell.lunar.day}",
                color = lunarTextColor,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
            )
            if (showCanChi) {
                Text(
                    text = cell.canChiLabel,
                    color = canChiTextColor,
                    fontSize = 7.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
            if (cell.isCurrentMonth) {
                when {
                    cell.holiday != null -> {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = cell.holiday,
                            color = holidayTextColor,
                            fontSize = 6.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    cell.solarTerm != null -> {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = cell.solarTerm,
                            color = solarTermTextColor,
                            fontSize = 6.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }

                    cell.lunar.day == 1 || cell.lunar.day == 15 -> {
                        Spacer(Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(lunarDotColor, CircleShape),
                        )
                    }
                }
            }
        }
    }
}
