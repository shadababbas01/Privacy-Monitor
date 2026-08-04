package com.privacymonitor.android.domain.repository

import com.privacymonitor.android.domain.model.PrivacyEvent
import com.privacymonitor.android.domain.model.RiskAssessment
import kotlinx.coroutines.flow.Flow

interface RiskRepository {
    fun observeLatestAssessment(): Flow<RiskAssessment?>
    suspend fun saveAssessment(assessment: RiskAssessment)
    fun observeEvents(): Flow<List<PrivacyEvent>>
    suspend fun addEvent(event: PrivacyEvent)
    suspend fun markEventResolved(eventId: String)
}
