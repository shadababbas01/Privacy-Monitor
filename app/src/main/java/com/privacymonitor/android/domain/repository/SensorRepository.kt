package com.privacymonitor.android.domain.repository

import com.privacymonitor.android.domain.model.SensorStatus
import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    fun observeSensorStatuses(): Flow<List<SensorStatus>>
    suspend fun getLiveSensorStatuses(): List<SensorStatus>
}
