package com.privacymonitor.android.domain.engine

import com.privacymonitor.android.domain.model.PermissionInfo
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.model.RiskReason
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiskEngine @Inject constructor() {

    data class EvaluationResult(
        val appScore: Int,
        val riskLevel: RiskLevel,
        val reasons: List<RiskReason>
    )

    fun evaluateApp(
        packageName: String,
        appName: String,
        isSystemApp: Boolean,
        installerPackage: String?,
        permissions: List<PermissionInfo>,
        specialAccessList: List<String>
    ): EvaluationResult {
        var score = 100
        val reasons = mutableListOf<RiskReason>()

        // Rule 1: Accessibility Access
        if (specialAccessList.contains("ACCESSIBILITY_SERVICE")) {
            val penalty = if (isSystemApp) 5 else 15
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "accessibility_access",
                    title = "Accessibility Service Access",
                    pointsDeducted = penalty,
                    explanation = "This app can observe your screen and simulate touches across all apps.",
                    recommendation = "Review Accessibility settings and disable if not explicitly needed.",
                    source = "Android Accessibility Manager"
                )
            )
        }

        // Rule 2: Background Location
        val hasBackgroundLocation = permissions.any {
            it.name.contains("ACCESS_BACKGROUND_LOCATION", ignoreCase = true) && it.isGranted
        }
        if (hasBackgroundLocation) {
            val penalty = if (isSystemApp) 4 else 12
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "background_location",
                    title = "Background Location Access",
                    pointsDeducted = penalty,
                    explanation = "This app can track your physical location even when closed.",
                    recommendation = "Change permission setting to 'Allow only while using the app'.",
                    source = "Android Permission Manager"
                )
            )
        }

        // Rule 3: Precise Location
        val hasFineLocation = permissions.any {
            it.name.contains("ACCESS_FINE_LOCATION", ignoreCase = true) && it.isGranted
        }
        if (hasFineLocation && !hasBackgroundLocation) {
            val penalty = if (isSystemApp) 2 else 6
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "fine_location",
                    title = "Precise Location Access",
                    pointsDeducted = penalty,
                    explanation = "This app can access your exact GPS coordinates.",
                    recommendation = "Use approximate location if fine accuracy is unnecessary.",
                    source = "Android Location Manager"
                )
            )
        }

        // Rule 4: SMS Permissions
        val hasSms = permissions.any {
            (it.name.contains("READ_SMS", ignoreCase = true) || it.name.contains("RECEIVE_SMS", ignoreCase = true)) && it.isGranted
        }
        if (hasSms) {
            val penalty = if (isSystemApp) 3 else 10
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "sms_permission",
                    title = "SMS Reading Permission",
                    pointsDeducted = penalty,
                    explanation = "App can read incoming SMS messages including potential OTPs.",
                    recommendation = "Verify why this app requires SMS access.",
                    source = "Android Telephony Manager"
                )
            )
        }

        // Rule 5: Call Logs & Phone
        val hasCallLog = permissions.any {
            it.name.contains("READ_CALL_LOG", ignoreCase = true) && it.isGranted
        }
        if (hasCallLog) {
            val penalty = if (isSystemApp) 3 else 10
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "call_log_permission",
                    title = "Call Log Access",
                    pointsDeducted = penalty,
                    explanation = "App can view phone call history and contact frequencies.",
                    recommendation = "Revoke call log access if app is not a phone dialer.",
                    source = "Android Contacts/Telephony"
                )
            )
        }

        // Rule 6: Overlay (Draw over other apps)
        if (specialAccessList.contains("SYSTEM_ALERT_WINDOW")) {
            val penalty = if (isSystemApp) 3 else 8
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "overlay_access",
                    title = "Display Over Other Apps",
                    pointsDeducted = penalty,
                    explanation = "App can display floating windows that could capture touch inputs.",
                    recommendation = "Turn off 'Display over other apps' in Android settings.",
                    source = "Android Window Manager"
                )
            )
        }

        // Rule 7: Unknown Installer Source
        val isTrustedInstaller = installerPackage != null && (
                installerPackage.contains("android.vending") ||
                        installerPackage.contains("google") ||
                        installerPackage.contains("amazon")
                )
        if (!isSystemApp && !isTrustedInstaller) {
            val penalty = 12
            score -= penalty
            reasons.add(
                RiskReason(
                    id = "unknown_installer",
                    title = "Sideloaded / Unknown Installer Source",
                    pointsDeducted = penalty,
                    explanation = "This app was not installed via the official Google Play Store.",
                    recommendation = "Ensure APK was downloaded from a verified, safe source.",
                    source = "Package Installer Metadata"
                )
            )
        }

        // Rule 8: Camera & Microphone
        val hasCamera = permissions.any { it.name.contains("CAMERA", ignoreCase = true) && it.isGranted }
        val hasMic = permissions.any { it.name.contains("RECORD_AUDIO", ignoreCase = true) && it.isGranted }
        if (hasCamera) {
            score -= 4
            reasons.add(
                RiskReason(
                    id = "camera_permission",
                    title = "Camera Permission Granted",
                    pointsDeducted = 4,
                    explanation = "App can capture photos and videos when active.",
                    recommendation = "Ensure camera access is needed for app features.",
                    source = "Android Camera Manager"
                )
            )
        }
        if (hasMic) {
            score -= 4
            reasons.add(
                RiskReason(
                    id = "mic_permission",
                    title = "Microphone Permission Granted",
                    pointsDeducted = 4,
                    explanation = "App can record audio when active.",
                    recommendation = "Check audio recording permissions.",
                    source = "Android Audio Manager"
                )
            )
        }

        val finalScore = score.coerceIn(0, 100)
        val riskLevel = when {
            finalScore >= 85 -> RiskLevel.SAFE
            finalScore >= 70 -> RiskLevel.MODERATE
            finalScore >= 50 -> RiskLevel.HIGH
            else -> RiskLevel.CRITICAL
        }

        return EvaluationResult(
            appScore = finalScore,
            riskLevel = riskLevel,
            reasons = reasons
        )
    }

    fun calculateOverallPrivacyScore(appScores: List<Int>): Int {
        if (appScores.isEmpty()) return 100

        val lowestScore = appScores.minOrNull() ?: 100
        val averageScore = appScores.average()

        // Weighted overall score heavily influenced by the highest-risk apps installed
        val overall = (lowestScore * 0.4 + averageScore * 0.6).toInt()
        return overall.coerceIn(0, 100)
    }
}
