package com.privacymonitor.android.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_snapshots")
data class ScoreSnapshotEntity(
    @PrimaryKey val timestamp: Long,
    val overallScore: Int,
    val riskLevel: String,
    val totalAppsScanned: Int,
    val riskyAppsCount: Int,
    val locationAccessAppsCount: Int,
    val micAccessAppsCount: Int,
    val cameraAccessAppsCount: Int,
    val totalDataTodayBytes: Long
)
