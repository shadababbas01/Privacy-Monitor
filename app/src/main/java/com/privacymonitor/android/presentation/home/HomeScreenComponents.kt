package com.privacymonitor.android.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.privacymonitor.android.R
import com.privacymonitor.android.core.designsystem.CardBackgroundDark
import com.privacymonitor.android.core.designsystem.HighRiskRed
import com.privacymonitor.android.core.designsystem.HighRiskRedBg
import com.privacymonitor.android.core.designsystem.PrimaryPurple
import com.privacymonitor.android.core.designsystem.SafeTeal
import com.privacymonitor.android.core.designsystem.SafeTealBg
import com.privacymonitor.android.core.designsystem.SurfaceDark
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark
import com.privacymonitor.android.core.designsystem.WarningAmber
import com.privacymonitor.android.core.designsystem.WarningAmberBg
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.PrivacyEvent
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.model.SensorState
import com.privacymonitor.android.domain.model.SensorStatus

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "R",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.greeting_format, "Rahul"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = TextPrimaryDark,
                    modifier = Modifier.size(26.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(HighRiskRed)
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun ScoreCard(
    score: Int,
    riskyAppsCount: Int,
    riskLevel: RiskLevel,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(id = R.string.ai_privacy_score),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.displayLarge,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/100",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextSecondaryDark,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { score / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = when (riskLevel) {
                            RiskLevel.SAFE -> SafeTeal
                            RiskLevel.MODERATE -> WarningAmber
                            RiskLevel.HIGH, RiskLevel.CRITICAL -> HighRiskRed
                        },
                        strokeWidth = 6.dp,
                        trackColor = SurfaceDark,
                    )
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.risky_apps_detected, riskyAppsCount),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            val (badgeBg, badgeTextColor, badgeTextRes) = when (riskLevel) {
                RiskLevel.SAFE -> Triple(SafeTealBg, SafeTeal, R.string.risk_good)
                RiskLevel.MODERATE -> Triple(WarningAmberBg, WarningAmber, R.string.risk_moderate)
                RiskLevel.HIGH -> Triple(HighRiskRedBg, HighRiskRed, R.string.risk_high)
                RiskLevel.CRITICAL -> Triple(HighRiskRedBg, HighRiskRed, R.string.risk_critical)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = badgeTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(id = badgeTextRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = badgeTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun OverviewSection(
    installedAppsCount: Int,
    locationAccessCount: Int,
    micAccessCount: Int,
    dataTodayBytes: Long
) {
    Column {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Apps,
                countText = "$installedAppsCount",
                title = stringResource(id = R.string.installed_apps),
                badgeText = "+2",
                badgeBg = PrimaryPurple.copy(alpha = 0.2f),
                badgeColor = PrimaryPurple
            )
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MyLocation,
                countText = "$locationAccessCount",
                title = stringResource(id = R.string.location_access),
                badgeText = "High",
                badgeBg = HighRiskRedBg,
                badgeColor = HighRiskRed
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Mic,
                countText = "$micAccessCount",
                title = stringResource(id = R.string.mic_access),
                badgeText = "Risk",
                badgeBg = WarningAmberBg,
                badgeColor = WarningAmber
            )
            OverviewCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Wifi,
                countText = String.format("%.1f GB", dataTodayBytes / (1024f * 1024f * 1024f)),
                title = stringResource(id = R.string.data_today),
                badgeText = "OK",
                badgeBg = SafeTealBg,
                badgeColor = SafeTeal
            )
        }
    }
}

@Composable
fun OverviewCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    countText: String,
    title: String,
    badgeText: String,
    badgeBg: Color,
    badgeColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextSecondaryDark,
                    modifier = Modifier.size(22.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = badgeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = countText,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryDark
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = actionText,
            style = MaterialTheme.typography.labelLarge,
            color = PrimaryPurple,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun RiskyAppItem(app: InstalledApp, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = app.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = app.permissions.filter { it.isGranted }.take(3).joinToString(" · ") { it.category },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark
                    )
                }
            }

            val (riskBg, riskText) = when (app.riskLevel) {
                RiskLevel.SAFE -> Pair(SafeTealBg, SafeTeal)
                RiskLevel.MODERATE -> Pair(WarningAmberBg, WarningAmber)
                RiskLevel.HIGH, RiskLevel.CRITICAL -> Pair(HighRiskRedBg, HighRiskRed)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(riskBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = app.riskLevel.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = riskText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LiveSensorsGrid(sensors: List<SensorStatus>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sensors.take(3).forEach { sensor ->
            SensorCard(modifier = Modifier.weight(1f), sensor = sensor)
        }
    }
}

@Composable
fun SensorCard(modifier: Modifier = Modifier, sensor: SensorStatus) {
    val (icon, activeColor) = when (sensor.id) {
        "gps" -> Pair(Icons.Default.MyLocation, PrimaryPurple)
        "network" -> Pair(Icons.Default.CellTower, PrimaryPurple)
        "camera" -> Pair(Icons.Default.CameraAlt, TextSecondaryDark)
        "microphone" -> Pair(Icons.Default.Mic, HighRiskRed)
        "bluetooth" -> Pair(Icons.Default.Bluetooth, PrimaryPurple)
        else -> Pair(Icons.Default.Shield, TextSecondaryDark)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (sensor.state == SensorState.ACTIVE) activeColor else TextSecondaryDark,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = sensor.name,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimaryDark,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = sensor.state.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = if (sensor.state == SensorState.ACTIVE) activeColor else TextSecondaryDark
            )
        }
    }
}

@Composable
fun PrivacyAlertItem(alert: PrivacyEvent) {
    val bulletColor = when (alert.severity) {
        RiskLevel.SAFE -> SafeTeal
        RiskLevel.MODERATE -> WarningAmber
        RiskLevel.HIGH, RiskLevel.CRITICAL -> HighRiskRed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(bulletColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${alert.appName} — ${alert.title}",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = alert.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark
                )
            }
        }
    }
}

@Composable
fun ScanCtaCard(isLoading: Boolean, onScanClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isLoading) onScanClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = PrimaryPurple,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = TextPrimaryDark,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.full_scan_cta),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimaryDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ScanProgressDialog(progressState: ScanProgressState) {
    if (!progressState.isScanning) return

    val animatedProgress by animateFloatAsState(
        targetValue = progressState.progressPercent,
        animationSpec = tween(durationMillis = 350),
        label = "ScanProgress"
    )

    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "प्राइवेसी स्कैन जारी है",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = progressState.currentStepText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryDark
                )

                if (progressState.scannedAppsCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${progressState.scannedAppsCount} ऐप्स की जांच हुई",
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryPurple,
                    trackColor = SurfaceDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
