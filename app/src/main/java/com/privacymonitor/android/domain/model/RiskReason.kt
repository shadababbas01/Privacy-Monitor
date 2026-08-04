package com.privacymonitor.android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RiskReason(
    val id: String,
    val title: String,
    val pointsDeducted: Int,
    val explanation: String,
    val recommendation: String,
    val source: String,
    val observedTimestamp: Long = System.currentTimeMillis()
)
