package com.privacymonitor.android.domain.model

data class PrivacyEvent(
    val id: String,
    val packageName: String,
    val appName: String,
    val title: String,
    val subtitle: String,
    val timestamp: Long,
    val severity: RiskLevel,
    val sensorType: String? = null,
    val isResolved: Boolean = false
)
