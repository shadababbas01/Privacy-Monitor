package com.privacymonitor.android.domain.repository

data class AdvisorResponse(
    val answer: String,
    val recommendedAction: String?,
    val source: String
)

interface PrivacyAdvisor {
    suspend fun explain(question: String, packageName: String? = null): AdvisorResponse
}
