package com.privacymonitor.android.presentation.navigation

sealed class Screen(val route: String, val title: String? = null) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home", "Home")
    object Apps : Screen("apps", "Apps")
    object AppDetail : Screen("app_detail/{packageName}") {
        fun createRoute(packageName: String) = "app_detail/$packageName"
    }
    object Reports : Screen("reports", "Reports")
    object Sensors : Screen("sensors", "Sensors")
    object Advisor : Screen("advisor", "AI Advisor")
    object Settings : Screen("settings", "Settings")
}
