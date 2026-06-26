package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.components.ObserveOnLifecycleOwner
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.calendar.state.CalendarEffect
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun CalendarScreen(onNavigateToToday: (SolarDate) -> Unit) {
    val viewModel: CalendarViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveOnLifecycleOwner {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CalendarEffect.NavigateToToday -> {
                    onNavigateToToday(effect.date)
                }
            }
        }
    }

    CalendarContent(state = state, onIntent = viewModel::onIntent)
}
