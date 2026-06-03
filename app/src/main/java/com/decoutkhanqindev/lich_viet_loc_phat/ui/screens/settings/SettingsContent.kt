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
import com.decoutkhanqindev.lich_viet_loc_phat.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDo
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoDark
import com.decoutkhanqindev.lich_viet_loc_phat.theme.GiayDoMid
import com.decoutkhanqindev.lich_viet_loc_phat.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.theme.VangDongLight
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.AppCard
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
            .background(Brush.verticalGradient(listOf(GiayDo, GiayDoMid, GiayDoDark))),
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
                    onCheckedChange = { onIntent(SettingsIntent.ToggleCanChiOnCell(it)) },
                )
            }

            SettingsGroup(title = "Tiện Ích") {
                SettingsToggleRow(
                    label = "Widget lịch",
                    subtitle = "Hiển thị lịch tháng trên màn hình chính",
                    checked = state.calendarWidgetEnabled,
                    onCheckedChange = { onIntent(SettingsIntent.ToggleCalendarWidget(it)) },
                )
            }

            SettingsGroup(title = "Về Ứng Dụng") {
                SettingsInfoRow(label = "Phiên bản", value = state.appVersion)
                HorizontalDivider(color = BorderWarm, modifier = Modifier.padding(vertical = 2.dp))
                SettingsInfoRow(label = "Thuật toán", value = "Hồ Ngọc Đức")
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            title.uppercase(),
            color = VangDong.copy(alpha = 0.85f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp),
        )
        AppCard {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
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
                .padding(end = 12.dp)
        ) {
            Text(label, color = MucDen, fontSize = 14.sp)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(subtitle, color = NauNhat, fontSize = 11.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VangDong,
                checkedTrackColor = VangDongLight.copy(alpha = 0.35f),
                uncheckedThumbColor = NauNhat,
                uncheckedTrackColor = NauNhat.copy(alpha = 0.2f),
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
        Text(label, color = MucDen, fontSize = 14.sp)
        Text(value, color = NauAm, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
