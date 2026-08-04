package com.privacymonitor.android.presentation.apps

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.privacymonitor.android.presentation.home.RiskyAppItem

@Composable
fun AppsScreen(
    viewModel: AppsViewModel,
    onAppClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavyDark)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.installed_apps),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search installed apps…", color = TextSecondaryDark) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondaryDark) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackgroundDark,
                unfocusedContainerColor = CardBackgroundDark,
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = CardBackgroundDark,
                focusedTextColor = TextPrimaryDark,
                unfocusedTextColor = TextPrimaryDark
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(AppFilter.values()) { filter ->
                FilterChip(
                    selected = state.selectedFilter == filter,
                    onClick = { viewModel.onFilterSelected(filter) },
                    label = {
                        Text(
                            filter.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                            color = if (state.selectedFilter == filter) TextPrimaryDark else TextSecondaryDark
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryPurple,
                        containerColor = CardBackgroundDark
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Apps List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.apps) { app ->
                RiskyAppItem(app = app, onClick = { onAppClick(app.packageName) })
            }
        }
    }
}
