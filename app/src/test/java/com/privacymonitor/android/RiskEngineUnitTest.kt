package com.privacymonitor.android

import com.privacymonitor.android.domain.engine.RiskEngine
import com.privacymonitor.android.domain.model.PermissionInfo
import com.privacymonitor.android.domain.model.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RiskEngineUnitTest {

    private lateinit var riskEngine: RiskEngine

    @Before
    fun setUp() {
        riskEngine = RiskEngine()
    }

    @Test
    fun `evaluateApp with no permissions returns 100 score and SAFE level`() {
        val result = riskEngine.evaluateApp(
            packageName = "com.example.safe",
            appName = "SafeApp",
            isSystemApp = false,
            installerPackage = "com.android.vending",
            permissions = emptyList(),
            specialAccessList = emptyList()
        )

        assertEquals(100, result.appScore)
        assertEquals(RiskLevel.SAFE, result.riskLevel)
        assertTrue(result.reasons.isEmpty())
    }

    @Test
    fun `evaluateApp with accessibility and background location deducts risk points`() {
        val permissions = listOf(
            PermissionInfo("android.permission.ACCESS_BACKGROUND_LOCATION", isGranted = true, isSensitive = true, category = "Location", description = ""),
            PermissionInfo("android.permission.CAMERA", isGranted = true, isSensitive = true, category = "Camera", description = "")
        )
        val specialAccess = listOf("ACCESSIBILITY_SERVICE")

        val result = riskEngine.evaluateApp(
            packageName = "com.example.risky",
            appName = "RiskyApp",
            isSystemApp = false,
            installerPackage = null,
            permissions = permissions,
            specialAccessList = specialAccess
        )

        // Deductions: Accessibility (-15), Background Location (-12), Camera (-4), Sideloaded (-12) = 100 - 43 = 57
        assertEquals(57, result.appScore)
        assertEquals(RiskLevel.HIGH, result.riskLevel)
        assertEquals(4, result.reasons.size)
    }

    @Test
    fun `calculateOverallPrivacyScore handles empty list safely`() {
        val overall = riskEngine.calculateOverallPrivacyScore(emptyList())
        assertEquals(100, overall)
    }

    @Test
    fun `calculateOverallPrivacyScore gives proper weight to lowest score`() {
        val scores = listOf(100, 90, 40) // lowest is 40, average is 76.66
        val overall = riskEngine.calculateOverallPrivacyScore(scores)
        // 40 * 0.4 + 76.66 * 0.6 = 16 + 46 = 62
        assertEquals(62, overall)
    }
}
