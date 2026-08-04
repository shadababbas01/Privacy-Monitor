package com.privacymonitor.android.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installed_apps")
data class InstalledAppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val versionName: String,
    val isSystemApp: Boolean,
    val installerPackageName: String?,
    val firstInstallTime: Long,
    val lastUpdateTime: Long,
    val permissionsJson: String,
    val specialAccessJson: String,
    val riskScore: Int,
    val riskLevel: String,
    val riskReasonsJson: String,
    val totalDataUsageBytes: Long,
    val isUpiOrFinancial: Boolean
)
