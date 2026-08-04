package com.privacymonitor.android.domain.repository

import com.privacymonitor.android.domain.model.InstalledApp
import kotlinx.coroutines.flow.Flow

interface InstalledAppRepository {
    suspend fun getInstalledApps(): List<InstalledApp>
    fun observeInstalledApps(): Flow<List<InstalledApp>>
    suspend fun getAppDetails(packageName: String): InstalledApp?
    suspend fun refreshAppsScan(): List<InstalledApp>
}
