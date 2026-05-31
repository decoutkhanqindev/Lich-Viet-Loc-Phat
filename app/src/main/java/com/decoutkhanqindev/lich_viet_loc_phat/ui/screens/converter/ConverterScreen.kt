package com.decoutkhanqindev.lich_viet_loc_phat.ui.screens.converter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConverterScreen() {
    val viewModel: ConverterViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    ConverterContent(state = state, onIntent = viewModel::onIntent)
}