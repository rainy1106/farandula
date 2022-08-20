package com.mtc.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class ConnectionLiveData(private val context: Context) : LiveData<ConnectionModel>() {

    private var networkJob: Job? = null

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            broadcastNetworkState(intent)
        }
    }

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private fun broadcastNetworkState(intent: Intent) {
        networkJob?.cancel()
        networkJob = CoroutineScope(Dispatchers.IO).launch {
            delay(300)
            if (intent.extras != null) {
                val activeNetwork =
                    intent.extras?.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo?
                val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
                if (isConnected) {
                    when (activeNetwork?.type) {
                        ConnectivityManager.TYPE_WIFI -> postValue(
                            ConnectionModel(ConnectionType.WIFI, true)
                        )
                        ConnectivityManager.TYPE_MOBILE -> postValue(
                            ConnectionModel(ConnectionType.CELLULAR, true)
                        )
                        ConnectivityManager.TYPE_ETHERNET -> postValue(
                            ConnectionModel(ConnectionType.ETHERNET, true)
                        )
                    }
                } else {
                    postValue(ConnectionModel(ConnectionType.NONE, false))
                }
            }
        }
    }
}
