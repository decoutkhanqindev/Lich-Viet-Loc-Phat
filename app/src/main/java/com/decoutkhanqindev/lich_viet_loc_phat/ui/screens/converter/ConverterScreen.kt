package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.ConvertMode
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BaTrauDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassTint
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldLight
import com.decoutkhanqindev.lich_viet_loc_phat.theme.IvoryWhite
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauToi
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.GlassCard
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.onClick
import com.decoutkhanqindev.lich_viet_loc_phat.ui.model.ConvertResultUiModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ConverterScreen() {
    val activity = LocalContext.current as ComponentActivity
    val viewModel: ConverterViewModel = koinViewModel(viewModelStoreOwner = activity)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val solarToLunarSelected by remember {
        derivedStateOf { state.mode == ConvertMode.SOLAR_TO_LUNAR }
    }
    val lunarToSolarSelected by remember {
        derivedStateOf { state.mode == ConvertMode.LUNAR_TO_SOLAR }
    }
    val modeLabel by remember {
        derivedStateOf {
            if (state.mode == ConvertMode.SOLAR_TO_LUNAR) "Dương Lịch" else "Âm Lịch"
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConverterContract.Effect.ScrollResultIntoView -> {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BaTrauDark, NauToi))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Mode segmented control
            GlassCard {
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
                        onClick = {
                            viewModel.onIntent(
                                ConverterContract.Intent.ChangeMode(ConvertMode.SOLAR_TO_LUNAR)
                            )
                        },
                    )
                    ModeTab(
                        label = "Âm → Dương",
                        selected = lunarToSolarSelected,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.onIntent(
                                ConverterContract.Intent.ChangeMode(ConvertMode.LUNAR_TO_SOLAR)
                            )
                        },
                    )
                }
            }

            // Input card
            InputCard(
                modeLabel = modeLabel,
                inputDay = state.inputDay,
                inputMonth = state.inputMonth,
                inputYear = state.inputYear,
                lunarToSolarSelected = lunarToSolarSelected,
                isLeapMonth = state.isLeapMonth,
                error = state.error,
                onDayChanged = { d ->
                    viewModel.onIntent(
                        ConverterContract.Intent.InputChanged(
                            d,
                            state.inputMonth,
                            state.inputYear
                        )
                    )
                },
                onMonthChanged = { m ->
                    viewModel.onIntent(
                        ConverterContract.Intent.InputChanged(
                            state.inputDay,
                            m,
                            state.inputYear
                        )
                    )
                },
                onYearChanged = { y ->
                    viewModel.onIntent(
                        ConverterContract.Intent.InputChanged(
                            state.inputDay,
                            state.inputMonth,
                            y
                        )
                    )
                },
                onLeapMonthToggled = {
                    viewModel.onIntent(ConverterContract.Intent.ToggleLeapMonth(it))
                },
            )

            val shape = remember { RoundedCornerShape(12.dp) }
            // Convert button
            Box(
                modifier = Modifier
                    .onClick(shape) {
                        if (!state.isLoading) viewModel.onIntent(ConverterContract.Intent.Convert)
                    }
                    .fillMaxWidth()
                    .height(52.dp)
                    .alpha(if (state.isLoading) 0.6f else 1f)
                    .background(GoldAccent, shape),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = state.isLoading,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith
                                fadeOut(tween(180))
                    },
                    label = "ConvertButtonTransition",
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "Chuyển Đổi",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Result card
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
    GlassCard {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modeLabel,
                color = GoldAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberPicker(
                    label = "Ngày",
                    value = inputDay,
                    range = 1..31,
                    modifier = Modifier.weight(1f),
                    onChanged = onDayChanged,
                )
                NumberPicker(
                    label = "Tháng",
                    value = inputMonth,
                    range = 1..12,
                    modifier = Modifier.weight(1f),
                    onChanged = onMonthChanged,
                )
                NumberPicker(
                    label = "Năm",
                    value = inputYear,
                    range = 1900..2100,
                    modifier = Modifier.weight(1.5f),
                    onChanged = onYearChanged,
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
                            checkedColor = GoldAccent,
                            uncheckedColor = IvoryWhite.copy(alpha = 0.6f),
                        ),
                    )
                    Text("Tháng Nhuận", color = IvoryWhite, fontSize = 14.sp)
                }
            }

            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(150)),
            ) {
                Text(error ?: "", color = Color(0xFFFF6B6B), fontSize = 13.sp)
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
    val bg = remember(selected) {
        if (selected) GoldAccent.copy(alpha = 0.25f) else Color.Transparent
    }
    val textColor = remember(selected) {
        if (selected) GoldLight else IvoryWhite.copy(alpha = 0.6f)
    }
    val border = remember(selected) {
        if (selected) GoldAccent.copy(alpha = 0.5f) else Color.Transparent
    }

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
    val shape = remember { RoundedCornerShape(8.dp) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            color = IvoryWhite.copy(alpha = 0.6f),
            fontSize = 11.sp
        )
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                "−",
                color = GoldAccent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .onClick { if (value > range.first) onChanged(value - 1) }
                    .size(28.dp)
            )
            Box(
                modifier = Modifier
                    .border(1.dp, GlassBorder, shape)
                    .background(GlassTint, shape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "$value",
                    color = IvoryWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
            Text(
                "+",
                color = GoldAccent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .onClick { if (value < range.last) onChanged(value + 1) }
                    .size(28.dp)
            )
        }
    }
}

@Composable
private fun ResultCard(result: ConvertResultUiModel) {
    GlassCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Kết Quả",
                color = GoldAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
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
                    color = IvoryWhite.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                )
            }
            result.leapMonthNote?.let {
                Text(it, color = GoldAccent, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ResultField(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = IvoryWhite.copy(alpha = 0.55f), fontSize = 11.sp)
        Text(value, color = IvoryWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
    }
}
