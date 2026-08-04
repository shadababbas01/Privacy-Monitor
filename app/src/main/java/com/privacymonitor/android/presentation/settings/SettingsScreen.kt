package com.privacymonitor.android.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.privacymonitor.android.core.designsystem.TextPrimaryDark
import com.privacymonitor.android.core.designsystem.TextSecondaryDark

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nav_settings),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "App Language", style = MaterialTheme.typography.titleMedium, color = TextPrimaryDark, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = state.language == "hi",
                                onClick = { viewModel.setLanguage("hi") },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple)
                            )
                            Text(text = "Hindi / Hinglish", color = TextPrimaryDark)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = state.language == "en",
                                onClick = { viewModel.setLanguage("en") },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple)
                            )
                            Text(text = "English", color = TextPrimaryDark)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(14.dp)) }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Cloud AI Advisor (Optional)", style = MaterialTheme.typography.titleMedium, color = TextPrimaryDark, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Off by default. When enabled, anonymized queries can use Gemini API.", style = MaterialTheme.typography.bodyMedium, color = TextSecondaryDark)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Enable Cloud AI", color = TextPrimaryDark, modifier = Modifier.weight(1f))
                            Switch(
                                checked = state.cloudAiOptIn,
                                onCheckedChange = { viewModel.setCloudAiOptIn(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple)
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(14.dp)) }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundDark)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "About Privacy Monitor", style = MaterialTheme.typography.titleMedium, color = TextPrimaryDark, fontWeight = FontWeight.Bold)
                        Text(text = "Version 1.0.0 (Build 1)", style = MaterialTheme.typography.bodyMedium, color = TextSecondaryDark)
                        Text(text = "100% Local-First Device Analysis", style = MaterialTheme.typography.bodyMedium, color = TextSecondaryDark)
                    }
                }
            }
        }
    }
}
