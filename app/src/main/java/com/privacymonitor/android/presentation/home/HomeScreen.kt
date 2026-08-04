package com.privacymonitor.android.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.privacymonitor.android.R
import com.privacymonitor.android.core.designsystem.CardBackgroundDark
import com.privacymonitor.android.core.designsystem.DeepNavyDark
import com.privacymonitor.android.core.designsystem.SafeTeal
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark
import com.privacymonitor.android.domain.model.RiskLevel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToApps: () -> Unit,
    onNavigateToSensors: () -> Unit,
    onNavigateToAppDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Full Privacy Scan Overlay Dialog
    ScanProgressDialog(progressState = state.scanProgress)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Top Header
        item {
            HomeHeader()
        }

        // AI Privacy Score Card
        item {
            ScoreCard(
                score = state.assessment?.overallScore ?: 100,
                riskyAppsCount = state.assessment?.riskyAppsCount ?: 0,
                riskLevel = state.assessment?.riskLevel ?: RiskLevel.SAFE,
                isLoading = state.isLoading
            )
        }

        // Overview Grid Cards
        item {
            OverviewSection(
                installedAppsCount = state.assessment?.totalAppsScanned ?: 0,
                locationAccessCount = state.assessment?.locationAccessAppsCount ?: 0,
                micAccessCount = state.assessment?.micAccessAppsCount ?: 0,
                dataTodayBytes = state.assessment?.totalDataTodayBytes ?: 0L
            )
        }

        // Risky Apps Header & List
        item {
            SectionHeader(
                title = stringResource(id = R.string.risky_apps),
                actionText = stringResource(id = R.string.sab_dekho),
                onActionClick = onNavigateToApps
            )
        }

        if (state.riskyApps.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Koi high-risk app nahi mili. Aapka phone safe hai!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SafeTeal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        } else {
            items(state.riskyApps) { app ->
                RiskyAppItem(app = app, onClick = { onNavigateToAppDetail(app.packageName) })
            }
        }

        // Live Sensors Header & Grid
        item {
            SectionHeader(
                title = stringResource(id = R.string.live_sensors),
                actionText = stringResource(id = R.string.details),
                onActionClick = onNavigateToSensors
            )
        }

        item {
            LiveSensorsGrid(sensors = state.sensorStatuses)
        }

        // Recent Privacy Alerts
        item {
            SectionHeader(
                title = stringResource(id = R.string.recent_alerts),
                actionText = stringResource(id = R.string.sab_dekho),
                onActionClick = onNavigateToApps
            )
        }

        if (state.recentAlerts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Koi suspicious event detect nahi hua.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        } else {
            items(state.recentAlerts) { alert ->
                PrivacyAlertItem(alert = alert)
            }
        }

        // Full Privacy Scan CTA Button
        item {
            ScanCtaCard(
                isLoading = state.isLoading,
                onScanClick = { viewModel.runFullScan() }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
