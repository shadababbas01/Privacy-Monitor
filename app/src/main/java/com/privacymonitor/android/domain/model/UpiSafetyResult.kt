package com.privacymonitor.android.domain.model

data class UpiSafetyResult(
    val packageName: String,
    val appName: String,
    val isSafe: Boolean,
    val overlayRisk: Boolean,
    val accessibilityRisk: Boolean,
    val smsRisk: Boolean,
    val unknownInstallerRisk: Boolean,
    val warningMessage: String?
)
