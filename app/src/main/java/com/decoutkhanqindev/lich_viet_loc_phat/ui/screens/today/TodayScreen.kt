package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TodayScreen(initialDate: SolarDate? = null) {
    val viewModel: TodayViewModel = koinViewModel(parameters = { parametersOf(initialDate) })
    val state by viewModel.state.collectAsStateWithLifecycle()

    TodayContent(state = state, onIntent = viewModel::onIntent)
}
