package com.privacymonitor.android.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "privacy_events")
data class PrivacyEventEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val appName: String,
    val title: String,
    val subtitle: String,
    val timestamp: Long,
    val severity: String,
    val sensorType: String?,
    val isResolved: Boolean
)
