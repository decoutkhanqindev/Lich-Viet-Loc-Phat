package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.CuoiTuan
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoLe
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDenFaded
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmFaded
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmMedium
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmSubtle
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhatFaded
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBich
import com.decoutkhanqindev.lich_viet_loc_phat.theme.PickerFadeBottomBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.PickerFadeTopBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.PickerSeparatorBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellBrush
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFg
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgMuted
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgSecondary
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgTertiary
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongFaint
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongSelected
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongSoft
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongSubtle
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape10dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape12dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape20dp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.roundedCornerShape8dp
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.PrevNextButtons
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.TodayButton
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.onClick
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.AnimationContentKey
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.CalendarProperties
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DayCellUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs


@Composable
fun CalendarContent(
    state: CalendarState,
    onIntent: (CalendarIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush),
    ) {
        CalendarMonthHeader(
            displayedMonth = state.displayedMonth,
            displayedYear = state.displayedYear,
            lunarYearLabel = state.lunarYearLabel,
            lunarMonthLabel = state.lunarMonthLabel,
            showTodayButton = state.showTodayButton,
            onToday = {
                onIntent(CalendarIntent.RequestToday)
            },
            onPrev = {
                onIntent(CalendarIntent.PrevMonth)
            },
            onNext = {
                onIntent(CalendarIntent.NextMonth)
            },
            onShowPicker = {
                onIntent(CalendarIntent.ShowMonthYearPicker)
            },
        )

        val weekdays = remember { CalendarProperties.weekdaysMonFirst }

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
                .border(
                    width = 1.dp,
                    color = BorderWarm,
                    shape = roundedCornerShape12dp
                )
                .background(color = SurfaceCard, shape = roundedCornerShape12dp)
                .padding(8.dp),
        ) {
            CalendarGrid(
                isLoading = state.isLoading,
                error = state.error,
                days = state.days,
                showCanChiOnCell = state.showCanChiOnCell,
                onDayClick = {
                    onIntent(CalendarIntent.SelectDay(it))
                },
            )
        }
    }

    if (state.showMonthYearPicker) {
        MonthYearPickerDialog(
            initialMonth = state.displayedMonth,
            initialYear = state.displayedYear,
            onDismiss = {
                onIntent(CalendarIntent.DismissMonthYearPicker)
            },
            onConfirm = { y, m ->
                onIntent(CalendarIntent.ConfirmMonthYear(y, m))
            },
        )
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
    onShowPicker: () -> Unit,
) {
    val monthNames = remember { CalendarProperties.months }

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
            modifier = Modifier
                .weight(0.7f)
                .onClick(roundedCornerShape8dp) {
                    onShowPicker()
                },
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
                        color = VangDongSoft,
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
        isLoading -> AnimationContentKey.Loading
        error != null -> AnimationContentKey.Error
        else -> AnimationContentKey.Content
    }

    AnimatedContent(
        targetState = contentKey,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(180)) },
        label = "CalendarGridTransition",
    ) { key ->
        when (key) {
            AnimationContentKey.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp),
                    color = VangDong
                )
            }

            AnimationContentKey.Error -> Box(
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

            AnimationContentKey.Content -> LazyVerticalGrid(
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
    val solarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> MucDenFaded
            cell.isToday -> TodayCellFg
            else -> MucDen
        }
    }
    val lunarTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> NauNhatFaded
            cell.isToday -> TodayCellFgSecondary
            else -> NauAmFaded
        }
    }
    val canChiTextColor = remember(cell.isCurrentMonth, cell.isToday) {
        when {
            !cell.isCurrentMonth -> VangDongSubtle
            cell.isToday -> TodayCellFgTertiary
            else -> VangDongAccent
        }
    }
    val holidayTextColor = remember(cell.isToday) {
        if (cell.isToday) TodayCellFg else DoLe
    }
    val solarTermTextColor = remember(cell.isToday) {
        if (cell.isToday) TodayCellFg else NgocBich
    }
    val lunarDotColor = remember(cell.isToday) {
        if (cell.isToday) TodayCellFgMuted else VangDongAccent
    }

    Box(
        modifier = modifier
            .onClick(roundedCornerShape10dp) { onClick() }
            .clip(roundedCornerShape10dp)
            .then(
                when {
                    cell.isToday -> Modifier.background(TodayCellBrush)

                    cell.isSelected -> Modifier.border(
                        1.5.dp,
                        VangDongSelected,
                        roundedCornerShape10dp
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

@Composable
private fun MonthYearPickerDialog(
    initialMonth: Int,
    initialYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (year: Int, month: Int) -> Unit,
) {
    var pickerMonth by remember { mutableIntStateOf(initialMonth) }
    var pickerYear by remember { mutableIntStateOf(initialYear) }
    val monthLabels = remember {
        CalendarProperties.months.toImmutableList()
    }
    val yearLabels = remember {
        CalendarProperties.pickerYears.toImmutableList()
    }
    val itemHeight = 44.dp
    val visibleCount = 5

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .background(
                    color = SurfaceCard,
                    shape = roundedCornerShape20dp
                )
                .border(
                    width = 1.dp,
                    color = BorderWarm,
                    shape = roundedCornerShape20dp
                )
                .padding(20.dp),
        ) {
            Text(
                text = "Chọn Tháng & Năm",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MucDen,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp,
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(itemHeight)
                        .background(
                            color = VangDongFaint,
                            shape = roundedCornerShape10dp
                        )
                        .border(
                            width = 0.5.dp,
                            color = VangDongBorder,
                            shape = roundedCornerShape10dp
                        )
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    PickerColumn(
                        items = monthLabels,
                        initialIndex = initialMonth - 1,
                        onItemSelected = {
                            pickerMonth = it + 1
                        },
                        modifier = Modifier.weight(1f),
                        itemHeight = itemHeight,
                        visibleCount = visibleCount,
                    )

                    Box(
                        modifier = Modifier
                            .size(
                                width = 1.dp,
                                height = itemHeight * visibleCount
                            )
                            .background(PickerSeparatorBrush)
                    )

                    PickerColumn(
                        items = yearLabels,
                        initialIndex = initialYear - CalendarProperties.PICKER_YEAR_MIN,
                        onItemSelected = {
                            pickerYear = CalendarProperties.PICKER_YEAR_MIN + it
                        },
                        modifier = Modifier.weight(1f),
                        itemHeight = itemHeight,
                        visibleCount = visibleCount,
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(itemHeight * 2)
                        .background(PickerFadeTopBrush)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(itemHeight * 2)
                        .background(PickerFadeBottomBrush)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .onClick(roundedCornerShape12dp) {
                            onDismiss()
                        }
                        .border(
                            width = 1.dp,
                            color = BorderWarm,
                            shape = roundedCornerShape12dp
                        )
                        .background(color = SurfaceCard, shape = roundedCornerShape12dp)
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Đóng",
                        color = NauNhat,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .onClick(roundedCornerShape12dp) {
                            onConfirm(pickerYear, pickerMonth)
                        }
                        .background(color = VangDong, shape = roundedCornerShape12dp)
                        .padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Xác nhận",
                        color = TodayCellFg,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PickerColumn(
    items: ImmutableList<String>,
    initialIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    visibleCount: Int = 5,
) {
    val loopMultiplier = 1000
    val count = items.size
    val totalCount = remember(count) { count * loopMultiplier }
    val halfCount = remember(visibleCount) { visibleCount / 2 }
    val startIndex = remember(initialIndex, count) {
        (loopMultiplier / 2) * count + initialIndex
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val centerVirtualIndex by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val viewportCenter = (info.viewportStartOffset + info.viewportEndOffset) / 2f
            info.visibleItemsInfo.minByOrNull { item ->
                abs(item.offset + item.size / 2f - viewportCenter)
            }?.index ?: startIndex
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            onItemSelected(centerVirtualIndex % count)
        }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(vertical = itemHeight * halfCount),
        modifier = modifier.height(itemHeight * visibleCount),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            count = totalCount,
            key = { it % count }
        ) { virtualIndex ->
            val realIndex = remember(virtualIndex) { virtualIndex % count }
            val distFromCenter = remember(virtualIndex, centerVirtualIndex) {
                abs(virtualIndex - centerVirtualIndex)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = items[realIndex],
                    color = when (distFromCenter) {
                        0 -> MucDen
                        1 -> NauAmMedium
                        else -> NauAmSubtle
                    },
                    fontSize = when (distFromCenter) {
                        0 -> 16.sp
                        1 -> 14.sp
                        else -> 13.sp
                    },
                    fontWeight = if (distFromCenter == 0) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
