package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.toTodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarEffect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun CalendarScreen(backStack: NavBackStack<NavKey>) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = LocalContext.current as ComponentActivity
    val viewModel: CalendarViewModel = koinViewModel(viewModelStoreOwner = activity)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is CalendarEffect.NavigateToToday -> {
                        backStack.add(effect.date.toTodayDestination())
                    }
                }
            }
        }
    }

    CalendarContent(state = state, onIntent = viewModel::onIntent)
}
