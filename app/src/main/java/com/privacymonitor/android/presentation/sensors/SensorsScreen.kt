package com.privacymonitor.android.presentation.sensors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.privacymonitor.android.core.designsystem.PrimaryPurple
import com.privacymonitor.android.core.designsystem.SafeTeal
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark
import com.privacymonitor.android.core.designsystem.WarningAmber

@Composable
fun SensorsScreen(viewModel: SensorsViewModel) {
    val sensors by viewModel.sensorStatuses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.live_sensors),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sensors) { sensor ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sensor.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = sensor.state.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (sensor.state.name == "ACTIVE") SafeTeal else TextSecondaryDark,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${sensor.appsWithAccessCount} apps have permission access",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )

                        sensor.limitationNote?.let { note ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Note: $note",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarningAmber
                            )
                        }
                    }
                }
            }
        }
    }
}
