package com.privacymonitor.android.data.system

import android.net.TrafficStats
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataMonitor @Inject constructor() {

    fun getTotalRxTxBytesToday(): Long {
        val rx = TrafficStats.getTotalRxBytes()
        val tx = TrafficStats.getTotalTxBytes()
        return if (rx != TrafficStats.UNSUPPORTED.toLong() && tx != TrafficStats.UNSUPPORTED.toLong()) {
            rx + tx
        } else {
            0L
        }
    }
}
