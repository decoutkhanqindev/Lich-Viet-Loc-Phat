package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BaTrauDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GlassBorder
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GoldAccent
import com.decoutkhanqindev.lich_viet_loc_phat.theme.IvoryWhite
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauToi
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.GlassCard
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.settings.state.SettingsState

@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BaTrauDark, NauToi))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsGroup(title = "Hiển Thị") {
                SettingsToggleRow(
                    label = "Hiển thị Can Chi trên ô lịch",
                    subtitle = "Hiện Can Chi của mỗi ngày trong lưới lịch tháng",
                    checked = state.showCanChiOnCell,
                    onCheckedChange = {
                        onIntent(SettingsIntent.ToggleCanChiOnCell(it))
                    },
                )
            }

            SettingsGroup(title = "Về Ứng Dụng") {
                SettingsInfoRow(label = "Phiên bản", value = state.appVersion)
                HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 4.dp))
                SettingsInfoRow(label = "Thuật toán", value = "Hồ Ngọc Đức")
                HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 4.dp))
                SettingsInfoRow(label = "Chế độ", value = "100% Offline")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            color = GoldAccent,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
        )
        GlassCard {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(label, color = IvoryWhite, fontSize = 14.sp)
            if (subtitle != null) {
                Text(subtitle, color = IvoryWhite.copy(alpha = 0.55f), fontSize = 11.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GoldAccent,
                checkedTrackColor = GoldAccent.copy(alpha = 0.4f),
                uncheckedThumbColor = IvoryWhite.copy(alpha = 0.5f),
                uncheckedTrackColor = IvoryWhite.copy(alpha = 0.15f),
            ),
        )
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = IvoryWhite, fontSize = 14.sp)
        Text(value, color = IvoryWhite.copy(alpha = 0.6f), fontSize = 14.sp)
    }
}
