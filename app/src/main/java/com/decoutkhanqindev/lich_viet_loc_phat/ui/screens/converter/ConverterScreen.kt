package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter.state.ConverterEffect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ConverterScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as ComponentActivity
    val viewModel: ConverterViewModel = koinViewModel(viewModelStoreOwner = activity)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ConverterEffect.ScrollResultIntoView -> {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            }
        }
    }

    ConverterContent(state = state, onIntent = viewModel::onIntent)
}