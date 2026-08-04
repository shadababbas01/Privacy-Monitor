package com.privacymonitor.android.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.privacymonitor.android.R
import com.privacymonitor.android.core.designsystem.CardBackgroundDark
import com.privacymonitor.android.core.designsystem.PrimaryPurple
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark

sealed class BottomNavItem(val screen: Screen, val icon: ImageVector, val labelResId: Int) {
    object Home : BottomNavItem(Screen.Home, Icons.Default.Home, R.string.nav_home)
    object Apps : BottomNavItem(Screen.Apps, Icons.Default.Apps, R.string.nav_apps)
    object Reports : BottomNavItem(Screen.Reports, Icons.Default.Assessment, R.string.nav_reports)
    object Sensors : BottomNavItem(Screen.Sensors, Icons.Default.Sensors, R.string.nav_sensors)
    object Settings : BottomNavItem(Screen.Settings, Icons.Default.Settings, R.string.nav_settings)
}

@Composable
fun PrivacyBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Apps,
        BottomNavItem.Reports,
        BottomNavItem.Sensors,
        BottomNavItem.Settings
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = CardBackgroundDark
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(id = item.labelResId)) },
                label = { Text(stringResource(id = item.labelResId)) },
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    unselectedIconColor = TextSecondaryDark,
                    unselectedTextColor = TextSecondaryDark,
                    indicatorColor = CardBackgroundDark
                ),
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
