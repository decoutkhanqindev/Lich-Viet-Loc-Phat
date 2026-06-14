package com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ObserveNetworkStatusUseCase
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.base.BaseViewModel
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state.MainEffect
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state.MainIntent
import com.decoutkhanqindev.lich_viet_loc_phat.presentation.screens.main.state.MainState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val observeNetworkStatus: ObserveNetworkStatusUseCase,
) : BaseViewModel<MainState, MainIntent, MainEffect>(MainState()) {
    init {
        viewModelScope.launch {
            observeNetworkStatus().collectLatest { result ->
                result.onSuccess { isOnline ->
                    updateState { copy(isOnline = isOnline) }
                }
            }
        }
    }

    override fun onIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.OpenNetworkSettings -> viewModelScope.launch {
                sendEffect(MainEffect.OpenNetworkSettings)
            }
        }
    }
}
