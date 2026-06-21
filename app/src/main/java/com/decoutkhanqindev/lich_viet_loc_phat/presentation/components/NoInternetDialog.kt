package com.decoutkhanqindev.lich_viet_loc_phat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.DoSon
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.DoSonAlpha12
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAmAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.TodayCellFg
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDong

@Composable
fun NoInternetDialog(onOpenWifiSettings: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        AppCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(color = DoSonAlpha12, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WifiOff,
                        contentDescription = null,
                        tint = DoSon,
                        modifier = Modifier.size(36.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.no_internet_title),
                    color = MucDen,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.no_internet_message),
                    color = NauAmAlpha70,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick(shape = RoundedCornerShape16dp) { onOpenWifiSettings() }
                        .background(color = VangDong, shape = RoundedCornerShape16dp)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.no_internet_open_settings),
                        color = TodayCellFg,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
