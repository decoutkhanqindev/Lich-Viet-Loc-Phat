package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderStrong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoSon
import com.decoutkhanqindev.lich_viet_loc_phat.theme.DoSonLight
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDo
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoMid
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.theme.SurfaceElevated
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongLight
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.AppCard
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.onClick
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.ConvertResultUiModel
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state.ConverterIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state.ConverterState

@Composable
fun ConverterContent(
    state: ConverterState,
    onIntent: (ConverterIntent) -> Unit,
) {
    val solarToLunarSelected = remember(state.mode) { state.mode == ConvertMode.SOLAR_TO_LUNAR }
    val lunarToSolarSelected = remember(state.mode) { state.mode == ConvertMode.LUNAR_TO_SOLAR }
    val modeLabel = remember(state.mode) {
        if (state.mode == ConvertMode.SOLAR_TO_LUNAR) "Dương Lịch" else "Âm Lịch"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GiayDo, GiayDoMid, GiayDoDark))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ModeTab(
                        label = "Dương → Âm",
                        selected = solarToLunarSelected,
                        modifier = Modifier.weight(1f),
                        onClick = { onIntent(ConverterIntent.ChangeMode(ConvertMode.SOLAR_TO_LUNAR)) },
                    )
                    ModeTab(
                        label = "Âm → Dương",
                        selected = lunarToSolarSelected,
                        modifier = Modifier.weight(1f),
                        onClick = { onIntent(ConverterIntent.ChangeMode(ConvertMode.LUNAR_TO_SOLAR)) },
                    )
                }
            }

            InputCard(
                modeLabel = modeLabel,
                inputDay = state.inputDay,
                inputMonth = state.inputMonth,
                inputYear = state.inputYear,
                lunarToSolarSelected = lunarToSolarSelected,
                isLeapMonth = state.isLeapMonth,
                error = state.error,
                onDayChanged = { d ->
                    onIntent(
                        ConverterIntent.InputChanged(
                            d,
                            state.inputMonth,
                            state.inputYear
                        )
                    )
                },
                onMonthChanged = { m ->
                    onIntent(
                        ConverterIntent.InputChanged(
                            state.inputDay,
                            m,
                            state.inputYear
                        )
                    )
                },
                onYearChanged = { y ->
                    onIntent(
                        ConverterIntent.InputChanged(
                            state.inputDay,
                            state.inputMonth,
                            y
                        )
                    )
                },
                onLeapMonthToggled = { onIntent(ConverterIntent.ToggleLeapMonth(it)) },
            )

            val buttonShape = remember { RoundedCornerShape(14.dp) }
            Box(
                modifier = Modifier
                    .onClick(buttonShape) { if (!state.isLoading) onIntent(ConverterIntent.Convert) }
                    .fillMaxWidth()
                    .height(54.dp)
                    .alpha(if (state.isLoading) 0.6f else 1f)
                    .background(
                        Brush.horizontalGradient(listOf(DoSon, DoSonLight)),
                        buttonShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = state.isLoading,
                    transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(180)) },
                    label = "ConvertButtonTransition",
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Chuyển Đổi",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.result != null,
                enter = fadeIn() + expandVertically(),
            ) {
                state.result?.let { ResultCard(it) }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InputCard(
    modeLabel: String,
    inputDay: Int,
    inputMonth: Int,
    inputYear: Int,
    lunarToSolarSelected: Boolean,
    isLeapMonth: Boolean,
    error: String?,
    onDayChanged: (Int) -> Unit,
    onMonthChanged: (Int) -> Unit,
    onYearChanged: (Int) -> Unit,
    onLeapMonthToggled: (Boolean) -> Unit,
) {
    AppCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modeLabel.uppercase(),
                color = VangDong,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberPicker(
                    label = "Ngày",
                    value = inputDay,
                    range = 1..31,
                    modifier = Modifier.weight(1f),
                    onChanged = onDayChanged
                )
                NumberPicker(
                    label = "Tháng",
                    value = inputMonth,
                    range = 1..12,
                    modifier = Modifier.weight(1f),
                    onChanged = onMonthChanged
                )
                NumberPicker(
                    label = "Năm",
                    value = inputYear,
                    range = 1900..2100,
                    modifier = Modifier.weight(1.5f),
                    onChanged = onYearChanged
                )
            }

            AnimatedVisibility(
                visible = lunarToSolarSelected,
                enter = fadeIn(tween(200)) + expandVertically(tween(200)),
                exit = fadeOut(tween(150)) + shrinkVertically(tween(150)),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isLeapMonth,
                        onCheckedChange = onLeapMonthToggled,
                        colors = CheckboxDefaults.colors(
                            checkedColor = VangDong,
                            uncheckedColor = NauNhat,
                        ),
                    )
                    Text("Tháng Nhuận", color = MucDen, fontSize = 14.sp)
                }
            }

            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(150)),
            ) {
                Text(error ?: "", color = DoSon, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun ModeTab(
    label: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val shape = remember { RoundedCornerShape(10.dp) }
    val bg = if (selected) VangDong.copy(alpha = 0.12f) else Color.Transparent
    val textColor = if (selected) VangDong else NauNhat
    val border = if (selected) VangDongLight.copy(alpha = 0.5f) else Color.Transparent

    Box(
        modifier = modifier
            .onClick(shape) { onClick() }
            .background(bg, shape)
            .border(1.dp, border, shape)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun NumberPicker(
    label: String,
    value: Int,
    range: IntRange,
    modifier: Modifier,
    onChanged: (Int) -> Unit,
) {
    val shape = remember { RoundedCornerShape(10.dp) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = NauNhat, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                "−",
                color = VangDong,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .onClick { if (value > range.first) onChanged(value - 1) }
                    .size(28.dp),
            )
            Box(
                modifier = Modifier
                    .border(1.dp, BorderStrong, shape)
                    .background(SurfaceElevated, shape)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "$value",
                    color = MucDen,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
            Text(
                "+",
                color = VangDong,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .onClick { if (value < range.last) onChanged(value + 1) }
                    .size(28.dp),
            )
        }
    }
}

@Composable
private fun ResultCard(result: ConvertResultUiModel) {
    AppCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "KẾT QUẢ",
                color = VangDong,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
            )
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ResultField("Ngày", result.dayLabel)
                ResultField("Tháng", result.monthLabel)
                ResultField("Năm", result.yearLabel)
            }
            result.canChi?.let { cc ->
                Spacer(Modifier.height(8.dp))
                Text(
                    "${cc.canNgay} ${cc.chiNgay} · ${cc.canThang} ${cc.chiThang} · ${cc.canNam} ${cc.chiNam}",
                    color = NauAm,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                )
            }
            result.leapMonthNote?.let {
                Text(it, color = VangDong, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ResultField(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = NauNhat, fontSize = 11.sp)
        Text(value, color = MucDen, fontSize = 32.sp, fontWeight = FontWeight.Light)
    }
}
