package com.privacymonitor.android

import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.PermissionInfo
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.usecase.CheckUpiSafetyUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckUpiSafetyUseCaseTest {

    private val useCase = CheckUpiSafetyUseCase()

    @Test
    fun `non financial app returns null`() {
        val app = InstalledApp(
            packageName = "com.example.calculator",
            appName = "Calculator",
            versionName = "1.0",
            isSystemApp = false,
            installerPackageName = "com.android.vending",
            firstInstallTime = 0L,
            lastUpdateTime = 0L,
            permissions = emptyList(),
            specialAccessList = emptyList(),
            riskScore = 100,
            riskLevel = RiskLevel.SAFE,
            riskReasons = emptyList()
        )

        val result = useCase(app)
        assertNull(result)
    }

    @Test
    fun `UPI app with accessibility flags overlay and safety risk`() {
        val app = InstalledApp(
            packageName = "net.one97.paytm",
            appName = "Paytm",
            versionName = "1.0",
            isSystemApp = false,
            installerPackageName = "com.android.vending",
            firstInstallTime = 0L,
            lastUpdateTime = 0L,
            permissions = emptyList(),
            specialAccessList = listOf("ACCESSIBILITY_SERVICE"),
            riskScore = 80,
            riskLevel = RiskLevel.MODERATE,
            riskReasons = emptyList(),
            isUpiOrFinancial = true
        )

        val result = useCase(app)
        assertNotNull(result)
        assertFalse(result!!.isSafe)
        assertTrue(result.accessibilityRisk)
    }
}
