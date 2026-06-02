package com.decoutkhanqindev.lich_viet_loc_phat.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun ObserveOnLifecycleOwner(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    onObserve: suspend () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnObserve by rememberUpdatedState(onObserve)

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(state) {
            currentOnObserve()
        }
    }
}