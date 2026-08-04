package com.privacymonitor.android.domain.model

data class WeeklyReport(
    val id: String,
    val weekStartDate: Long,
    val weekEndDate: Long,
    val initialScore: Int,
    val finalScore: Int,
    val sensitivePermissionAccessCount: Int,
    val newAppsInstalledCount: Int,
    val riskLevelChangesCount: Int,
    val summaryHinglish: String,
    val topRiskyApps: List<String>
)
