package com.privacymonitor.android.domain.model

data class InstalledApp(
    val packageName: String,
    val appName: String,
    val versionName: String,
    val isSystemApp: Boolean,
    val installerPackageName: String?,
    val firstInstallTime: Long,
    val lastUpdateTime: Long,
    val permissions: List<PermissionInfo>,
    val specialAccessList: List<String>,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val riskReasons: List<RiskReason>,
    val totalDataUsageBytes: Long = 0L,
    val isUpiOrFinancial: Boolean = false
)
