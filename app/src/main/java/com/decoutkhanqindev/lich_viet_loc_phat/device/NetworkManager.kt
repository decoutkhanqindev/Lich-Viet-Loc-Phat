package com.decoutkhanqindev.lich_viet_loc_phat.device

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.decoutkhanqindev.lich_viet_loc_phat.utils.Tag
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class NetworkManager(context: Context): Tag {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job + CoroutineExceptionHandler { _, throwable ->
        Timber.tag(tag).e(throwable.stackTraceToString())
    })

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val isConnected: Boolean
        get() = connectivityManager.activeNetworkInfo?.isConnected == true

    val available: StateFlow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(isConnected)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
        .distinctUntilChanged()
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = isConnected,
        )

    fun destroy() {
        job.cancel()
    }
}