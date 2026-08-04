package com.privacymonitor.android.data.system

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import com.privacymonitor.android.domain.model.SensorState
import com.privacymonitor.android.domain.model.SensorStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getSensorStatuses(installedAppsCountWithAccess: Map<String, Int>): List<SensorStatus> {
        val list = mutableListOf<SensorStatus>()

        // 1. GPS / Location
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        list.add(
            SensorStatus(
                id = "gps",
                name = "GPS Location",
                state = if (isGpsEnabled) SensorState.ACTIVE else SensorState.OFF,
                appsWithAccessCount = installedAppsCountWithAccess["Location"] ?: 0,
                limitationNote = "Android restricts third-party apps from reading exact location access logs in real-time."
            )
        )

        // 2. Camera
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        val hasCamera = try {
            (cameraManager?.cameraIdList?.size ?: 0) > 0
        } catch (e: Exception) {
            false
        }
        list.add(
            SensorStatus(
                id = "camera",
                name = "Camera Hardware",
                state = if (hasCamera) SensorState.OFF else SensorState.UNAVAILABLE,
                appsWithAccessCount = installedAppsCountWithAccess["Camera"] ?: 0,
                limitationNote = "Android 12+ displays a system green privacy indicator dot when active."
            )
        )

        // 3. Microphone
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        val isRecording = audioManager?.mode == AudioManager.MODE_IN_COMMUNICATION
        list.add(
            SensorStatus(
                id = "microphone",
                name = "Microphone",
                state = if (isRecording) SensorState.ACTIVE else SensorState.OFF,
                appsWithAccessCount = installedAppsCountWithAccess["Microphone"] ?: 0,
                limitationNote = "Android limits background microphone recording access."
            )
        )

        // 4. Bluetooth
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val isBtActive = bluetoothAdapter?.isEnabled == true
        list.add(
            SensorStatus(
                id = "bluetooth",
                name = "Bluetooth",
                state = if (isBtActive) SensorState.ACTIVE else SensorState.OFF,
                appsWithAccessCount = installedAppsCountWithAccess["Bluetooth"] ?: 0
            )
        )

        // 5. Gyroscope
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val gyroSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        list.add(
            SensorStatus(
                id = "gyro",
                name = "Gyroscope",
                state = if (gyroSensor != null) SensorState.OFF else SensorState.UNAVAILABLE,
                appsWithAccessCount = 0
            )
        )

        // 6. Network
        list.add(
            SensorStatus(
                id = "network",
                name = "Network Interface",
                state = SensorState.ACTIVE,
                appsWithAccessCount = installedAppsCountWithAccess["Network"] ?: 0
            )
        )

        return list
    }
}
