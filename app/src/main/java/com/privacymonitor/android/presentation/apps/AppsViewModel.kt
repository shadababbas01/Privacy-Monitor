package com.privacymonitor.android.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AppsUiState(
    val searchQuery: String = "",
    val selectedFilter: AppFilter = AppFilter.ALL,
    val apps: List<InstalledApp> = emptyList()
)

enum class AppFilter {
    ALL, HIGH_RISK, CAMERA, MIC, LOCATION, UPI
}

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val appRepository: InstalledAppRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(AppFilter.ALL)

    val uiState: StateFlow<AppsUiState> = combine(
        _searchQuery,
        _selectedFilter,
        appRepository.observeInstalledApps()
    ) { query, filter, apps ->
        val filtered = apps.filter { app ->
            val matchesQuery = app.appName.contains(query, ignoreCase = true) ||
                    app.packageName.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                AppFilter.ALL -> true
                AppFilter.HIGH_RISK -> app.riskLevel == RiskLevel.HIGH || app.riskLevel == RiskLevel.CRITICAL
                AppFilter.CAMERA -> app.permissions.any { it.name.contains("CAMERA") && it.isGranted }
                AppFilter.MIC -> app.permissions.any { it.name.contains("RECORD_AUDIO") && it.isGranted }
                AppFilter.LOCATION -> app.permissions.any { it.name.contains("LOCATION") && it.isGranted }
                AppFilter.UPI -> app.isUpiOrFinancial
            }
            matchesQuery && matchesFilter
        }

        AppsUiState(
            searchQuery = query,
            selectedFilter = filter,
            apps = filtered
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppsUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: AppFilter) {
        _selectedFilter.value = filter
    }
}
