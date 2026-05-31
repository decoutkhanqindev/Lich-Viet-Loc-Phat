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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BaTrauDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorderStrong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassTint
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.HolidayDot
import com.decoutkhanqindev.lich_viet_loc_phat.theme.IvoryWhite
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauToi
import com.decoutkhanqindev.lich_viet_loc_phat.theme.WeekendColor
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
            .background(Brush.verticalGradient(listOf(BaTrauDark, NauToi))),
    ) {
        CalendarMonthHeader(
            displayedMonth = state.displayedMonth,
            displayedYear = state.displayedYear,
            showTodayButton = state.showTodayButton,
            onToday = { onIntent(CalendarIntent.RequestToday) },
            onPrev = { onIntent(CalendarIntent.PrevMonth) },
            onNext = { onIntent(CalendarIntent.NextMonth) },
        )

        val weekdays = remember { listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            weekdays.forEachIndexed { idx, label ->
                Text(
                    label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = if (idx >= 5) WeekendColor else IvoryWhite.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(
                    1.dp,
                    GlassBorder,
                    RoundedCornerShape(16.dp)
                )
                .background(
                    GlassTint,
                    RoundedCornerShape(16.dp)
                )
                .padding(8.dp),
        ) {
            CalendarGrid(
                isLoading = state.isLoading,
                error = state.error,
                days = state.days,
                onDayClick = { onIntent(CalendarIntent.SelectDay(it)) },
            )
        }
    }
}

@Composable
private fun CalendarMonthHeader(
    displayedMonth: Int,
    displayedYear: Int,
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
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width(80.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showTodayButton,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(150)),
            ) {
                Icon(
                    Icons.Default.Today,
                    contentDescription = "Hôm nay",
                    tint = GoldAccent,
                    modifier = Modifier
                        .onClick { onToday() }
                        .size(28.dp)
                )
            }
        }

        AnimatedContent(
            targetState = displayedMonth to displayedYear,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                (slideInVertically { it / 3 } + fadeIn(tween(200))) togetherWith
                        (slideOutVertically { -it / 3 } + fadeOut(tween(150)))
            },
            label = "MonthYearTransition",
        ) { (month, year) ->
            Text(
                "${monthNames[month - 1]} · $year",
                textAlign = TextAlign.Center,
                color = IvoryWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

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
}

@Composable
private fun CalendarGrid(
    isLoading: Boolean,
    error: String?,
    days: ImmutableList<DayCellUiModel>,
    onDayClick: (SolarDate) -> Unit,
) {
    val contentKey = when {
        isLoading -> "loading"
        error != null -> "error"
        else -> "content"
    }
    AnimatedContent(
        targetState = contentKey,
        transitionSpec = {
            fadeIn(tween(220)) togetherWith
                    fadeOut(tween(180))
        },
        label = "CalendarGridTransition",
    ) { key ->
        when (key) {
            "loading" -> Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp),
                    color = GoldAccent,
                )
            }

            "error" -> Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "",
                    color = IvoryWhite,
                    modifier = Modifier.padding(32.dp),
                    textAlign = TextAlign.Center,
                )
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    items = days,
                    key = { "${it.solar.year}-${it.solar.month}-${it.solar.day}" },
                ) { cell ->
                    DayCell(
                        cell = cell,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasIndicator = remember(cell.lunar.day, cell.holiday) {
        cell.holiday != null || cell.lunar.day == 1 || cell.lunar.day == 15
    }

    val solarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> IvoryWhite.copy(alpha = 0.3f)
            cell.isToday -> Color.Black
            else -> IvoryWhite
        }
    }
    val lunarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> IvoryWhite.copy(alpha = 0.25f)
            cell.isToday -> Color.Black.copy(alpha = 0.7f)
            else -> IvoryWhite.copy(alpha = 0.7f)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (cell.isSelected && !cell.isToday)
                    Modifier.border(
                        1.5.dp,
                        GlassBorderStrong,
                        RoundedCornerShape(8.dp)
                    )
                else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        // Today: gold circle background
        if (cell.isToday) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        Brush.radialGradient(listOf(GoldAccent, GoldAccent.copy(alpha = 0.7f))),
                        CircleShape,
                    ),
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }

        // Dot indicator — holiday / rằm / mùng một
        if (hasIndicator && cell.isCurrentMonth) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp)
                    .size(4.dp)
                    .background(
                        if (cell.holiday != null) HolidayDot else GoldAccent,
                        CircleShape,
                    ),
            )
        }
    }
}