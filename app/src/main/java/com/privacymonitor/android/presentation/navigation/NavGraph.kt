package com.privacymonitor.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.privacymonitor.android.presentation.advisor.AdvisorViewModel
import com.privacymonitor.android.presentation.advisor.PrivacyAdvisorScreen
import com.privacymonitor.android.presentation.apps.AppDetailScreen
import com.privacymonitor.android.presentation.apps.AppsScreen
import com.privacymonitor.android.presentation.apps.AppsViewModel
import com.privacymonitor.android.presentation.home.HomeScreen
import com.privacymonitor.android.presentation.home.HomeViewModel
import com.privacymonitor.android.presentation.onboarding.OnboardingScreen
import com.privacymonitor.android.presentation.onboarding.OnboardingViewModel
import com.privacymonitor.android.presentation.reports.ReportsScreen
import com.privacymonitor.android.presentation.reports.ReportsViewModel
import com.privacymonitor.android.presentation.sensors.SensorsScreen
import com.privacymonitor.android.presentation.sensors.SensorsViewModel
import com.privacymonitor.android.presentation.settings.SettingsScreen
import com.privacymonitor.android.presentation.settings.SettingsViewModel

@Composable
fun PrivacyNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            val viewModel = hiltViewModel<OnboardingViewModel>()
            OnboardingScreen(
                viewModel = viewModel,
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                onNavigateToApps = { navController.navigate(Screen.Apps.route) },
                onNavigateToSensors = { navController.navigate(Screen.Sensors.route) },
                onNavigateToAppDetail = { pkg -> navController.navigate(Screen.AppDetail.createRoute(pkg)) }
            )
        }

        composable(Screen.Apps.route) {
            val viewModel = hiltViewModel<AppsViewModel>()
            AppsScreen(
                viewModel = viewModel,
                onAppClick = { pkg -> navController.navigate(Screen.AppDetail.createRoute(pkg)) }
            )
        }

        composable(
            route = Screen.AppDetail.route,
            arguments = listOf(navArgument("packageName") { type = NavType.StringType })
        ) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            val viewModel = hiltViewModel<AppsViewModel>()
            AppDetailScreen(
                packageName = packageName,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Reports.route) {
            val viewModel = hiltViewModel<ReportsViewModel>()
            ReportsScreen(viewModel = viewModel)
        }

        composable(Screen.Sensors.route) {
            val viewModel = hiltViewModel<SensorsViewModel>()
            SensorsScreen(viewModel = viewModel)
        }

        composable(Screen.Advisor.route) {
            val viewModel = hiltViewModel<AdvisorViewModel>()
            PrivacyAdvisorScreen(viewModel = viewModel)
        }

        composable(Screen.Settings.route) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(viewModel = viewModel)
        }
    }
}
