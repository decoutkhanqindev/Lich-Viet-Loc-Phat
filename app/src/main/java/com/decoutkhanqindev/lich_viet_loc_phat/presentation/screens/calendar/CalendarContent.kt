package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.PrevNextButtons
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.TodayButton
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads.NativeMedia169Ad
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.onClick
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.AnimationContentKey
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.CalendarProperties
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.model.DayCellUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.CuoiTuan
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.DoLe
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.MucDenAlpha25
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAmAlpha20
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAmAlpha65
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAmAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauNhatAlpha40
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NgocBich
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.PickerFadeBottomBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.PickerFadeTopBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.PickerSeparatorBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape10dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape20dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape8dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellFg
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellFgAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellFgAlpha80
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellFgAlpha85
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha20
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha25
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha50
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha75
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha8
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.koinInject
import kotlin.math.abs


@Composable
fun CalendarContent(
    state: CalendarState,
    onIntent: (CalendarIntent) -> Unit,
) {
    val adsManager: AdsManager = koinInject()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush)
            .verticalScroll(rememberScrollState()),
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
            onShowPicker = { onIntent(CalendarIntent.ShowMonthYearPicker) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 4.dp)
        ) {
            CalendarProperties.weekdaysMonFirst.forEachIndexed { idx, label ->
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
                    shape = RoundedCornerShape12dp
                )
                .background(color = SurfaceCard, shape = RoundedCornerShape12dp)
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

        NativeMedia169Ad(
            adUnit = adsManager.nativeCalendar,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 12.dp, bottom = 8.dp),
        )
    }

    if (state.showMonthYearPicker) {
        MonthYearPickerDialog(
            initialMonth = state.displayedMonth,
            initialYear = state.displayedYear,
            onDismiss = { onIntent(CalendarIntent.DismissMonthYearPicker) },
            onConfirm = { y, m -> onIntent(CalendarIntent.ConfirmMonthYear(y, m)) },
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
    val monthNames = remember {
        CalendarProperties.months.toImmutableList()
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
            modifier = Modifier
                .weight(0.7f)
                .onClick(RoundedCornerShape8dp) { onShowPicker() },
            transitionSpec = {
                (slideInVertically { it / 3 } + fadeIn(tween(200))) togetherWith
                        (slideOutVertically { -it / 3 } + fadeOut(tween(150)))
            },
            label = "MonthYearTransition",
        ) { hs ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(
                        R.string.calendar_month_year_format,
                        monthNames[hs.month - 1],
                        hs.year
                    ),
                    textAlign = TextAlign.Center,
                    color = MucDen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                if (hs.lunarYear != null && hs.lunarMonth != null) {
                    Text(
                        stringResource(
                            R.string.calendar_lunar_format,
                            hs.lunarYear,
                            hs.lunarMonth
                        ),
                        textAlign = TextAlign.Center,
                        color = VangDongAlpha75,
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
        transitionSpec = {
            fadeIn(tween(220)) togetherWith
                    fadeOut(tween(180))
        },
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

            AnimationContentKey.Content -> {
                val weeks = remember(days) { days.chunked(7) }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    weeks.forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                        ) {
                            week.forEach { cell ->
                                DayCell(
                                    cell = cell,
                                    showCanChi = showCanChiOnCell,
                                    onClick = { onDayClick(cell.solar) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
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
    val solarTextColor = when {
        !cell.isCurrentMonth -> MucDenAlpha25
        cell.isToday -> TodayCellFg
        else -> MucDen
    }
    val lunarTextColor = when {
        !cell.isCurrentMonth -> NauNhatAlpha40
        cell.isToday -> TodayCellFgAlpha85
        else -> NauAmAlpha70
    }
    val canChiTextColor = when {
        !cell.isCurrentMonth -> VangDongAlpha20
        cell.isToday -> TodayCellFgAlpha80
        else -> VangDongAlpha70
    }
    val holidayTextColor = if (cell.isToday) TodayCellFg else DoLe
    val solarTermTextColor = if (cell.isToday) TodayCellFg else NgocBich
    val lunarDotColor = if (cell.isToday) TodayCellFgAlpha70 else VangDongAlpha70
    val selectionBorderColor by animateColorAsState(
        targetValue = if (cell.isSelected) VangDongAlpha50 else Color.Transparent,
        animationSpec = tween(200),
        label = "DayCellSelectionBorder",
    )

    Box(
        modifier = modifier
            .onClick(RoundedCornerShape10dp) { onClick() }
            .clip(RoundedCornerShape10dp)
            .then(
                if (cell.isToday) {
                    Modifier.background(TodayCellBrush)
                } else {
                    Modifier.border(
                        width = 1.5.dp,
                        color = selectionBorderColor,
                        shape = RoundedCornerShape10dp
                    )
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
                DayCellMarker(
                    holiday = cell.holiday,
                    solarTerm = cell.solarTerm,
                    lunarDay = cell.lunar.day,
                    holidayTextColor = holidayTextColor,
                    solarTermTextColor = solarTermTextColor,
                    lunarDotColor = lunarDotColor,
                )
            }
        }
    }
}

@Composable
private fun DayCellMarker(
    holiday: String?,
    solarTerm: String?,
    lunarDay: Int,
    holidayTextColor: Color,
    solarTermTextColor: Color,
    lunarDotColor: Color,
) {
    when {
        holiday != null -> {
            Spacer(Modifier.height(2.dp))
            Text(
                text = holiday,
                color = holidayTextColor,
                fontSize = 6.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        solarTerm != null -> {
            Spacer(Modifier.height(2.dp))
            Text(
                text = solarTerm,
                color = solarTermTextColor,
                fontSize = 6.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }

        lunarDay == 1 || lunarDay == 15 -> {
            Spacer(Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(lunarDotColor, CircleShape),
            )
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
    val transitionState = remember { MutableTransitionState(false) }

    LaunchedEffect(Unit) {
        transitionState.targetState = true
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        AnimatedVisibility(
            visibleState = transitionState,
            enter = scaleIn() + fadeIn(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .background(
                        color = SurfaceCard,
                        shape = RoundedCornerShape20dp
                    )
                    .border(
                        width = 1.dp,
                        color = BorderWarm,
                        shape = RoundedCornerShape20dp
                    )
                    .padding(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.picker_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MucDen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp,
                )

                PickerWheels(
                    monthLabels = monthLabels,
                    yearLabels = yearLabels,
                    initialMonthIndex = initialMonth - 1,
                    initialYearIndex = initialYear - CalendarProperties.PICKER_YEAR_MIN,
                    onMonthSelected = { pickerMonth = it + 1 },
                    onYearSelected = { pickerYear = CalendarProperties.PICKER_YEAR_MIN + it },
                    itemHeight = itemHeight,
                    visibleCount = visibleCount,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DialogButton(
                        text = stringResource(R.string.action_close),
                        onClick = onDismiss,
                        isPrimary = false,
                        modifier = Modifier.weight(1f),
                    )

                    DialogButton(
                        text = stringResource(R.string.action_confirm),
                        onClick = { onConfirm(pickerYear, pickerMonth) },
                        isPrimary = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PickerWheels(
    monthLabels: ImmutableList<String>,
    yearLabels: ImmutableList<String>,
    initialMonthIndex: Int,
    initialYearIndex: Int,
    onMonthSelected: (Int) -> Unit,
    onYearSelected: (Int) -> Unit,
    itemHeight: Dp,
    visibleCount: Int,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = VangDongAlpha8,
                    shape = RoundedCornerShape10dp
                )
                .border(
                    width = 0.5.dp,
                    color = VangDongAlpha25,
                    shape = RoundedCornerShape10dp
                )
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            PickerColumn(
                items = monthLabels,
                initialIndex = initialMonthIndex,
                onItemSelected = onMonthSelected,
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
                initialIndex = initialYearIndex,
                onItemSelected = onYearSelected,
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
}

@Composable
private fun DialogButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
) {
    val baseModifier = modifier.onClick(RoundedCornerShape12dp) { onClick() }

    Box(
        modifier = if (isPrimary) {
            baseModifier
                .background(color = VangDong, shape = RoundedCornerShape12dp)
                .padding(vertical = 13.dp)
        } else {
            baseModifier
                .border(
                    width = 1.dp,
                    color = BorderWarm,
                    shape = RoundedCornerShape12dp
                )
                .background(color = SurfaceCard, shape = RoundedCornerShape12dp)
                .padding(vertical = 13.dp)
        },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (isPrimary) TodayCellFg else NauNhat,
            fontSize = 14.sp,
            fontWeight = if (isPrimary) FontWeight.SemiBold else FontWeight.Medium,
        )
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
    val totalCount = count * loopMultiplier
    val halfCount = visibleCount / 2
    val startIndex = (loopMultiplier / 2) * count + initialIndex

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
        if (!listState.isScrollInProgress) onItemSelected(centerVirtualIndex % count)
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
            key = { it }
        ) { virtualIndex ->
            val realIndex = virtualIndex % count
            val distFromCenter = abs(virtualIndex - centerVirtualIndex)

            PickerItem(
                label = items[realIndex],
                distFromCenter = distFromCenter,
                itemHeight = itemHeight,
            )
        }
    }
}

@Composable
private fun PickerItem(
    label: String,
    distFromCenter: Int,
    itemHeight: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = when (distFromCenter) {
                0 -> MucDen
                1 -> NauAmAlpha65
                else -> NauAmAlpha20
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
