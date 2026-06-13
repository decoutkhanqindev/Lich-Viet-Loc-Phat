package com.decoutkhanqindev.lich_viet_loc_phat.widget

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.decoutkhanqindev.lich_viet_loc_phat.MainActivity
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.CuoiTuan
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoLe
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoSon
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoMid
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDenAlpha25
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAmAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhatAlpha40
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NgocBich
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceCard
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFg
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgAlpha85
import com.decoutkhanqindev.lich_viet_loc_phat.theme.TodayCellFgAlpha80
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongAlpha75
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongAlpha20
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.CalendarProperties
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.DayCellUiModel
import kotlinx.collections.immutable.ImmutableList


@SuppressLint("RestrictedApi")
@Composable
fun CalendarWidgetContent(
    displayedMonth: Int,
    displayedYear: Int,
    lunarYearLabel: String?,
    lunarMonthLabel: String?,
    days: ImmutableList<DayCellUiModel>,
    showCanChiOnCell: Boolean,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(ColorProvider(GiayDoMid))
            .clickable {
                actionStartActivity<MainActivity>()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarMonthHeader(
            displayedMonth = displayedMonth,
            displayedYear = displayedYear,
            lunarYearLabel = lunarYearLabel,
            lunarMonthLabel = lunarMonthLabel,
        )

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
            ) {
                CalendarProperties.weekdaysMonFirst.forEachIndexed { idx, label ->
                    Text(
                        label,
                        modifier = GlanceModifier.defaultWeight(),
                        style = TextStyle(
                            color = ColorProvider(if (idx >= 5) CuoiTuan else NauAm),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }

            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(ColorProvider(BorderWarm))
                    .cornerRadius(12.dp)
                    .padding(1.dp),
            ) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(ColorProvider(SurfaceCard))
                        .cornerRadius(11.dp)
                        .padding(8.dp),
                ) {
                    CalendarGrid(
                        days = days,
                        showCanChiOnCell = showCanChiOnCell,
                    )
                }
            }
        }

        Text(
            LocalContext.current.getString(R.string.app_name),
            style = TextStyle(
                color = ColorProvider(VangDong),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            modifier = GlanceModifier.padding(top = 4.dp),
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun CalendarMonthHeader(
    displayedMonth: Int,
    displayedYear: Int,
    lunarYearLabel: String?,
    lunarMonthLabel: String?,
) {
    val context = LocalContext.current

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(GlanceModifier.defaultWeight())

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                context.getString(
                    R.string.calendar_month_year_format,
                    CalendarProperties.months[displayedMonth - 1],
                    displayedYear,
                ),
                style = TextStyle(
                    color = ColorProvider(MucDen),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ),
            )
            if (lunarYearLabel != null && lunarMonthLabel != null) {
                Text(
                    context.getString(
                        R.string.calendar_lunar_format,
                        lunarYearLabel,
                        lunarMonthLabel,
                    ),
                    style = TextStyle(
                        color = ColorProvider(VangDongAlpha75),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }

        Spacer(GlanceModifier.defaultWeight())
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun CalendarGrid(
    days: ImmutableList<DayCellUiModel>,
    showCanChiOnCell: Boolean,
) {
    Column(modifier = GlanceModifier.fillMaxWidth()) {
        days.chunked(7).forEach { week ->
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                week.forEach { cell ->
                    DayCell(
                        cell = cell,
                        showCanChi = showCanChiOnCell,
                        modifier = GlanceModifier.defaultWeight(),
                    )
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun DayCell(
    cell: DayCellUiModel,
    showCanChi: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    val solarTextColor = when {
        !cell.isCurrentMonth -> ColorProvider(MucDenAlpha25)
        cell.isToday -> ColorProvider(TodayCellFg)
        else -> ColorProvider(MucDen)
    }
    val lunarTextColor = when {
        !cell.isCurrentMonth -> ColorProvider(NauNhatAlpha40)
        cell.isToday -> ColorProvider(TodayCellFgAlpha85)
        else -> ColorProvider(NauAmAlpha70)
    }
    val canChiTextColor = when {
        !cell.isCurrentMonth -> ColorProvider(VangDongAlpha20)
        cell.isToday -> ColorProvider(TodayCellFgAlpha80)
        else -> ColorProvider(VangDongAlpha70)
    }
    val holidayTextColor =
        if (cell.isToday) ColorProvider(TodayCellFg) else ColorProvider(DoLe)
    val solarTermTextColor =
        if (cell.isToday) ColorProvider(TodayCellFg) else ColorProvider(NgocBich)
    val lunarDotColor =
        if (cell.isToday) ColorProvider(TodayCellFgAlpha70)
        else ColorProvider(VangDongAlpha70)
    val modifier = if (cell.isToday) {
        modifier
            .padding(1.dp)
            .background(ColorProvider(DoSon))
            .cornerRadius(10.dp)
    } else {
        modifier.padding(1.dp)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = GlanceModifier.padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "${cell.solar.day}",
                style = TextStyle(
                    color = solarTextColor,
                    fontSize = 15.sp,
                    fontWeight = if (cell.isToday) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
            )
            Text(
                text = if (cell.lunar.day == 1) "1/${cell.lunar.month}" else "${cell.lunar.day}",
                style = TextStyle(
                    color = lunarTextColor,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                ),
            )
            if (showCanChi) {
                Text(
                    text = cell.canChiLabel,
                    style = TextStyle(
                        color = canChiTextColor,
                        fontSize = 7.sp,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                )
            }
            if (cell.isCurrentMonth) {
                when {
                    cell.holiday != null -> {
                        Spacer(GlanceModifier.height(2.dp))
                        Text(
                            text = cell.holiday,
                            style = TextStyle(
                                color = holidayTextColor,
                                fontSize = 6.sp,
                                textAlign = TextAlign.Center,
                            ),
                            maxLines = 1,
                        )
                    }

                    cell.solarTerm != null -> {
                        Spacer(GlanceModifier.height(2.dp))
                        Text(
                            text = cell.solarTerm,
                            style = TextStyle(
                                color = solarTermTextColor,
                                fontSize = 6.sp,
                                textAlign = TextAlign.Center,
                            ),
                            maxLines = 1,
                        )
                    }

                    cell.lunar.day == 1 || cell.lunar.day == 15 -> {
                        Spacer(GlanceModifier.height(3.dp))
                        Box(
                            modifier = GlanceModifier
                                .size(4.dp)
                                .background(lunarDotColor)
                                .cornerRadius(2.dp),
                        ) {}
                    }
                }
            }
        }
    }
}
