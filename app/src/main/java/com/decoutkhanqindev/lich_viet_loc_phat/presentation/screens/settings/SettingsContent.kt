package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppCard
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.BorderWarm
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAm
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauNhat
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauNhatAlpha20
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha85
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongLightAlpha35

@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsGroup(title = stringResource(R.string.settings_group_display)) {
                SettingsToggleRow(
                    label = stringResource(R.string.settings_can_chi_label),
                    subtitle = stringResource(R.string.settings_can_chi_subtitle),
                    checked = state.showCanChiOnCell,
                    onCheckedChange = { onIntent(SettingsIntent.ToggleCanChiOnCell(it)) },
                )
            }

            SettingsGroup(title = stringResource(R.string.settings_group_utilities)) {
                SettingsToggleRow(
                    label = stringResource(R.string.settings_widget_label),
                    subtitle = stringResource(R.string.settings_widget_subtitle),
                    checked = state.calendarWidgetEnabled,
                    onCheckedChange = { onIntent(SettingsIntent.ToggleCalendarWidget(it)) },
                )
            }

            SettingsGroup(title = stringResource(R.string.settings_group_about)) {
                SettingsInfoRow(
                    label = stringResource(R.string.settings_version),
                    value = state.appVersion
                )

                HorizontalDivider(color = BorderWarm, modifier = Modifier.padding(vertical = 2.dp))

                SettingsInfoRow(
                    label = stringResource(R.string.settings_algorithm),
                    value = stringResource(R.string.settings_algorithm_value)
                )
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
            color = VangDongAlpha85,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp),
        )
        AppCard {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                )
            ) { content() }
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
            Text(
                label,
                color = MucDen,
                fontSize = 14.sp
            )

            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    color = NauNhat,
                    fontSize = 11.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VangDong,
                checkedTrackColor = VangDongLightAlpha35,
                uncheckedThumbColor = NauNhat,
                uncheckedTrackColor = NauNhatAlpha20,
            ),
        )
    }
}

@Composable
private fun SettingsInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            color = MucDen,
            fontSize = 14.sp
        )

        Text(
            value,
            color = NauAm,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
