package com.decoutkhanqindev.lich_viet_loc_phat.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S, I, E>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onIntent(intent: I)

    protected fun updateState(block: S.() -> S) = _state.update { it.block() }

    protected suspend fun sendEffect(effect: E) = _effect.send(effect)

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }
}
