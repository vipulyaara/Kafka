package com.kafka.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import org.rekhta.base.debug
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(@ApplicationContext private val context: Context) {
    enum class NetworkStatus { Available, Unavailable }

    private val connectivityManager: ConnectivityManager = context.getSystemService()!!
    val networkStatus = MutableStateFlow(connectivityManager.activeNetwork?.toStatus())

    init {
        addNetworkCallback()
    }

    private fun Network.toStatus() = if (connectivityManager.getNetworkCapabilities(this)
            ?.hasCapability(NET_CAPABILITY_INTERNET) == true
    ) {
        NetworkStatus.Available
    } else {
        NetworkStatus.Unavailable
    }

    private fun addNetworkCallback() = connectivityManager.registerNetworkCallback(
        NetworkRequest.Builder().build(),
        object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
                debug { "Network onUnavailable" }
                networkStatus.tryEmit(NetworkStatus.Unavailable)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                debug { "Network onLost" }
                networkStatus.tryEmit(NetworkStatus.Unavailable)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                debug { "Network onAvailable" }
                networkStatus.tryEmit(NetworkStatus.Available)
            }
        }
    )
}
