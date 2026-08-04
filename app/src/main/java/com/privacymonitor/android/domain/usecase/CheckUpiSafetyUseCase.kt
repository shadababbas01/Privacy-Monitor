package com.privacymonitor.android.domain.usecase

import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.UpiSafetyResult
import javax.inject.Inject

class CheckUpiSafetyUseCase @Inject constructor() {

    private val knownUpiPackages = setOf(
        "com.phonepe.app",
        "net.one97.paytm",
        "com.google.android.apps.nbu.paisa.user",
        "in.org.npci.upiapp",
        "com.whatsapp",
        "com.icicibank.mobilebanking",
        "com.sbi.upi"
    )

    operator fun invoke(app: InstalledApp): UpiSafetyResult? {
        val isUpiApp = app.isUpiOrFinancial || knownUpiPackages.contains(app.packageName)
        if (!isUpiApp) return null

        val hasOverlay = app.specialAccessList.contains("SYSTEM_ALERT_WINDOW")
        val hasAccessibility = app.specialAccessList.contains("ACCESSIBILITY_SERVICE")
        val hasSms = app.permissions.any { it.name.contains("SMS") && it.isGranted }
        val isSideloaded = app.installerPackageName == null || !app.installerPackageName.contains("vending")

        val isSafe = !hasOverlay && !hasAccessibility && !isSideloaded

        val warning = when {
            hasAccessibility -> "Accessibility service enabled — financial app input can be recorded."
            hasOverlay -> "Screen overlay permitted — screen recording or overlay fraud risk."
            isSideloaded -> "Installed from an unverified source — official update recommended."
            else -> null
        }

        return UpiSafetyResult(
            packageName = app.packageName,
            appName = app.appName,
            isSafe = isSafe,
            overlayRisk = hasOverlay,
            accessibilityRisk = hasAccessibility,
            smsRisk = hasSms,
            unknownInstallerRisk = isSideloaded,
            warningMessage = warning
        )
    }
}
