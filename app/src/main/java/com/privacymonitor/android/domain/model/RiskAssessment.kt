package com.privacymonitor.android.domain.model

data class RiskAssessment(
    val overallScore: Int,
    val riskLevel: RiskLevel,
    val totalAppsScanned: Int,
    val riskyAppsCount: Int,
    val locationAccessAppsCount: Int,
    val micAccessAppsCount: Int,
    val cameraAccessAppsCount: Int,
    val totalDataTodayBytes: Long,
    val topRiskyApps: List<InstalledApp>,
    val scanTimestamp: Long = System.currentTimeMillis()
)
