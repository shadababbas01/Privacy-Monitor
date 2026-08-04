package com.privacymonitor.android

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.privacymonitor.android.core.designsystem.PrivacyMonitorTheme
import com.privacymonitor.android.data.local.datastore.UserPreferencesRepository
import com.privacymonitor.android.presentation.navigation.PrivacyBottomBar
import com.privacymonitor.android.presentation.navigation.PrivacyNavGraph
import com.privacymonitor.android.presentation.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val language by userPreferencesRepository.appLanguage.collectAsState(initial = "hi")
            val onboardingCompleted by userPreferencesRepository.onboardingCompleted.collectAsState(initial = false)

            updateLocale(this, language)

            PrivacyMonitorTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Apps.route,
                    Screen.Reports.route,
                    Screen.Sensors.route,
                    Screen.Settings.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            PrivacyBottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    val startDest = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route
                    PrivacyNavGraph(
                        navController = navController,
                        startDestination = startDest,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun updateLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
