package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.today

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.lich_viet_loc_phat.domain.model.SolarDate
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.viewmodel.koinActivityViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TodayScreen(initialDate: SolarDate? = null) {
    val viewModel: TodayViewModel = koinActivityViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(initialDate) {
        initialDate?.let { viewModel.setInitialDate(it) }
    }

    TodayContent(state = state, onIntent = viewModel::onIntent)
}
