package com.decoutkhanqindev.lich_viet_loc_phat.device.hardware

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InternetHardwareImpl(private val context: Context) : InternetHardware {
    override fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        val activeNetwork = connectivityManager.activeNetworkInfo
        trySend(activeNetwork?.isConnected == true)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}