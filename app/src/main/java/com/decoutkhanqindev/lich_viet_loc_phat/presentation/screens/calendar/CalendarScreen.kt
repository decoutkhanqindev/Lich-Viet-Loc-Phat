package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.navigation.toTodayDestination
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun CalendarScreen(onNavigateToTab: (NavKey) -> Unit) {
    val viewModel: CalendarViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveOnLifecycleOwner {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CalendarEffect.NavigateToToday -> onNavigateToTab(effect.date.toTodayDestination())
            }
        }
    }

    CalendarContent(state = state, onIntent = viewModel::onIntent)
}
