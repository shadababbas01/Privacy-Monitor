package com.privacymonitor.android.data.system

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.privacymonitor.android.R

class LocalVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == "STOP_VPN") {
            stopVpn()
            return START_NOT_STICKY
        }

        startForegroundNotification()
        setupVpn()
        return START_STICKY
    }

    private fun setupVpn() {
        try {
            vpnInterface = Builder()
                .addAddress("10.0.0.2", 32)
                .addRoute("0.0.0.0", 0)
                .setSession("PrivacyMonitorVpn")
                .establish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startForegroundNotification() {
        val channelId = "privacy_vpn_channel"
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Privacy Network Protection", NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Local Privacy Protection Active")
            .setContentText("Monitoring local destination IPs without payload decryption.")
            .setSmallIcon(R.drawable.ic_privacy_shield)
            .setOngoing(true)
            .build()

        startForeground(1001, notification)
    }

    private fun stopVpn() {
        vpnInterface?.close()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }
}
