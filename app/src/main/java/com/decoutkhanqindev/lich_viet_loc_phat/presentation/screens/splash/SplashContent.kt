package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.lich_viet_loc_phat.R
import com.decoutkhanqindev.lich_viet_loc_phat.ads.AdsManager
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.AppLottie
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ads.BannerAd
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.GiayDoBrush
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.MucDen
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.NauAmAlpha70
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDong
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.theme.VangDongAlpha20
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SplashContent() {
    val adsManager: AdsManager = koinInject()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GiayDoBrush),
    ) {
        LogoAndSlogan(modifier = Modifier.align(Alignment.Center))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyMedium,
                color = NauAmAlpha70,
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                color = VangDong,
                trackColor = VangDongAlpha20,
            )

            BannerAd(
                adUnit = adsManager.bannerSplash,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun LogoAndSlogan(modifier: Modifier = Modifier) {
    val enterSpec = remember { tween<Float>(durationMillis = 900, easing = FastOutSlowInEasing) }
    val exitSpec = remember { tween<Float>(durationMillis = 700, easing = FastOutSlowInEasing) }
    val visiblePauseMs = 800L
    val cycleGapMs = 300L
    val offsetY = -80f
    val appNameAlpha = remember { Animatable(0f) }
    val appNameOffsetY = remember { Animatable(offsetY) }
    val sloganAlpha = remember { Animatable(0f) }
    val sloganOffsetY = remember { Animatable(offsetY) }

    LaunchedEffect(Unit) {
        while (true) {
            launch { sloganOffsetY.animateTo(0f, enterSpec) }
            sloganAlpha.animateTo(1f, enterSpec)

            launch { appNameOffsetY.animateTo(0f, enterSpec) }
            appNameAlpha.animateTo(1f, enterSpec)

            delay(visiblePauseMs)

            launch { sloganOffsetY.animateTo(offsetY, exitSpec) }
            sloganAlpha.animateTo(0f, exitSpec)

            launch { appNameOffsetY.animateTo(offsetY, exitSpec) }
            appNameAlpha.animateTo(0f, exitSpec)

            delay(cycleGapMs)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppLottie(
            modifier = Modifier
                .size(156.dp)
                .padding(8.dp),
            resId = R.raw.lich_bloc_loc_phat,
        )

        Text(
            text = stringResource(R.string.vietnamese_calendar),
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = FontStyle.Italic,
            color = NauAmAlpha70,
            modifier = Modifier.graphicsLayer(
                alpha = sloganAlpha.value,
                translationY = sloganOffsetY.value,
            ),
        )

        Text(
            text = stringResource(R.string.loc_phat),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MucDen,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .graphicsLayer {
                    alpha = appNameAlpha.value
                    translationY = appNameOffsetY.value
                },
        )
    }
}