package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.navigation.toTodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.ui.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.calendar.state.CalendarEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun CalendarScreen(backStack: NavBackStack<NavKey>) {
    val viewModel: CalendarViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveOnLifecycleOwner {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CalendarEffect.NavigateToToday -> {
                    backStack.add(effect.date.toTodayDestination())
                }
            }
        }
    }

    CalendarContent(state = state, onIntent = viewModel::onIntent)
}
