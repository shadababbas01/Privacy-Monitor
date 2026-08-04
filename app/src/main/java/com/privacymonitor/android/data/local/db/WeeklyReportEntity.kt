package com.privacymonitor.android.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_reports")
data class WeeklyReportEntity(
    @PrimaryKey val id: String,
    val weekStartDate: Long,
    val weekEndDate: Long,
    val initialScore: Int,
    val finalScore: Int,
    val sensitivePermissionAccessCount: Int,
    val newAppsInstalledCount: Int,
    val riskLevelChangesCount: Int,
    val summaryHinglish: String,
    val topRiskyAppsJson: String
)
