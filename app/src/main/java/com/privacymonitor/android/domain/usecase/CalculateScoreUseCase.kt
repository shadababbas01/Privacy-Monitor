package com.privacymonitor.android.domain.usecase

import com.privacymonitor.android.domain.engine.RiskEngine
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.RiskAssessment
import com.privacymonitor.android.domain.model.RiskLevel
import javax.inject.Inject

class CalculateScoreUseCase @Inject constructor(
    private val riskEngine: RiskEngine
) {
    operator fun invoke(apps: List<InstalledApp>): RiskAssessment {
        val appScores = apps.map { it.riskScore }
        val overallScore = riskEngine.calculateOverallPrivacyScore(appScores)

        val riskyApps = apps.filter { it.riskLevel == RiskLevel.HIGH || it.riskLevel == RiskLevel.CRITICAL }
            .sortedBy { it.riskScore }

        val locationCount = apps.count { app ->
            app.permissions.any { it.name.contains("LOCATION", ignoreCase = true) && it.isGranted }
        }
        val micCount = apps.count { app ->
            app.permissions.any { it.name.contains("RECORD_AUDIO", ignoreCase = true) && it.isGranted }
        }
        val cameraCount = apps.count { app ->
            app.permissions.any { it.name.contains("CAMERA", ignoreCase = true) && it.isGranted }
        }
        val totalData = apps.sumOf { it.totalDataUsageBytes }

        val overallLevel = when {
            overallScore >= 85 -> RiskLevel.SAFE
            overallScore >= 70 -> RiskLevel.MODERATE
            overallScore >= 50 -> RiskLevel.HIGH
            else -> RiskLevel.CRITICAL
        }

        return RiskAssessment(
            overallScore = overallScore,
            riskLevel = overallLevel,
            totalAppsScanned = apps.size,
            riskyAppsCount = riskyApps.size,
            locationAccessAppsCount = locationCount,
            micAccessAppsCount = micCount,
            cameraAccessAppsCount = cameraCount,
            totalDataTodayBytes = totalData,
            topRiskyApps = riskyApps.take(5)
        )
    }
}
