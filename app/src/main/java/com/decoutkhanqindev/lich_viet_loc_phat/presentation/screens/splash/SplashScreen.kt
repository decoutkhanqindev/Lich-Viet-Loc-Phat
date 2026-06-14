package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(onNavigateToToday: () -> Unit) {
    SplashContent(
        onNavigateToToday = onNavigateToToday,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}
