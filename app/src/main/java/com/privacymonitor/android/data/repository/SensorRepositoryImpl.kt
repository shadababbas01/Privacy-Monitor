package com.privacymonitor.android.data.repository

import com.privacymonitor.android.data.system.SensorMonitor
import com.privacymonitor.android.domain.model.SensorStatus
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import com.privacymonitor.android.domain.repository.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorRepositoryImpl @Inject constructor(
    private val sensorMonitor: SensorMonitor,
    private val appRepository: InstalledAppRepository
) : SensorRepository {

    override fun observeSensorStatuses(): Flow<List<SensorStatus>> = flow {
        emit(getLiveSensorStatuses())
    }

    override suspend fun getLiveSensorStatuses(): List<SensorStatus> {
        val apps = appRepository.getInstalledApps()
        val counts = mutableMapOf<String, Int>()

        counts["Location"] = apps.count { app ->
            app.permissions.any { it.name.contains("LOCATION", ignoreCase = true) && it.isGranted }
        }
        counts["Camera"] = apps.count { app ->
            app.permissions.any { it.name.contains("CAMERA", ignoreCase = true) && it.isGranted }
        }
        counts["Microphone"] = apps.count { app ->
            app.permissions.any { it.name.contains("RECORD_AUDIO", ignoreCase = true) && it.isGranted }
        }
        counts["Bluetooth"] = apps.count { app ->
            app.permissions.any { it.name.contains("BLUETOOTH", ignoreCase = true) && it.isGranted }
        }
        counts["Network"] = apps.size

        return sensorMonitor.getSensorStatuses(counts)
    }
}
