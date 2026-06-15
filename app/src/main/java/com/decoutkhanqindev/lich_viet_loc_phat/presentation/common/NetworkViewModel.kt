package com.decoutkhanqindev.lich_viet_loc_phat.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.lich_viet_loc_phat.domain.usecase.ObserveNetworkAvailableUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkViewModel(observeNetworkAvailable: ObserveNetworkAvailableUseCase) : ViewModel() {
    private val _available = MutableStateFlow(true)
    val available: StateFlow<Boolean> = _available.asStateFlow()

    init {
        viewModelScope.launch {
            observeNetworkAvailable().collectLatest { result ->
                result.onSuccess { available ->
                    _available.value = available
                }
            }
        }
    }
}