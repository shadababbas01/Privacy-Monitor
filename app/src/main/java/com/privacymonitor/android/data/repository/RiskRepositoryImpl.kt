package com.privacymonitor.android.data.repository

import com.privacymonitor.android.data.local.db.EventDao
import com.privacymonitor.android.data.local.db.PrivacyEventEntity
import com.privacymonitor.android.data.local.db.ScoreDao
import com.privacymonitor.android.data.local.db.ScoreSnapshotEntity
import com.privacymonitor.android.domain.model.PrivacyEvent
import com.privacymonitor.android.domain.model.RiskAssessment
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.repository.RiskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiskRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao,
    private val eventDao: EventDao
) : RiskRepository {

    override fun observeLatestAssessment(): Flow<RiskAssessment?> {
        return scoreDao.observeLatestScore().map { snapshot ->
            if (snapshot == null) null
            else RiskAssessment(
                overallScore = snapshot.overallScore,
                riskLevel = try { RiskLevel.valueOf(snapshot.riskLevel) } catch (e: Exception) { RiskLevel.SAFE },
                totalAppsScanned = snapshot.totalAppsScanned,
                riskyAppsCount = snapshot.riskyAppsCount,
                locationAccessAppsCount = snapshot.locationAccessAppsCount,
                micAccessAppsCount = snapshot.micAccessAppsCount,
                cameraAccessAppsCount = snapshot.cameraAccessAppsCount,
                totalDataTodayBytes = snapshot.totalDataTodayBytes,
                topRiskyApps = emptyList(),
                scanTimestamp = snapshot.timestamp
            )
        }
    }

    override suspend fun saveAssessment(assessment: RiskAssessment) {
        val entity = ScoreSnapshotEntity(
            timestamp = assessment.scanTimestamp,
            overallScore = assessment.overallScore,
            riskLevel = assessment.riskLevel.name,
            totalAppsScanned = assessment.totalAppsScanned,
            riskyAppsCount = assessment.riskyAppsCount,
            locationAccessAppsCount = assessment.locationAccessAppsCount,
            micAccessAppsCount = assessment.micAccessAppsCount,
            cameraAccessAppsCount = assessment.cameraAccessAppsCount,
            totalDataTodayBytes = assessment.totalDataTodayBytes
        )
        scoreDao.insertScoreSnapshot(entity)
    }

    override fun observeEvents(): Flow<List<PrivacyEvent>> {
        return eventDao.observeAllEvents().map { list ->
            list.map { entity ->
                PrivacyEvent(
                    id = entity.id,
                    packageName = entity.packageName,
                    appName = entity.appName,
                    title = entity.title,
                    subtitle = entity.subtitle,
                    timestamp = entity.timestamp,
                    severity = try { RiskLevel.valueOf(entity.severity) } catch (e: Exception) { RiskLevel.MODERATE },
                    sensorType = entity.sensorType,
                    isResolved = entity.isResolved
                )
            }
        }
    }

    override suspend fun addEvent(event: PrivacyEvent) {
        val entity = PrivacyEventEntity(
            id = event.id,
            packageName = event.packageName,
            appName = event.appName,
            title = event.title,
            subtitle = event.subtitle,
            timestamp = event.timestamp,
            severity = event.severity.name,
            sensorType = event.sensorType,
            isResolved = event.isResolved
        )
        eventDao.insertEvent(entity)
    }

    override suspend fun markEventResolved(eventId: String) {
        eventDao.markResolved(eventId)
    }
}
