package com.privacymonitor.android.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.core.util.DispatcherProvider
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.PrivacyEvent
import com.privacymonitor.android.domain.model.RiskAssessment
import com.privacymonitor.android.domain.model.SensorStatus
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import com.privacymonitor.android.domain.repository.RiskRepository
import com.privacymonitor.android.domain.repository.SensorRepository
import com.privacymonitor.android.domain.usecase.CalculateScoreUseCase
import com.privacymonitor.android.domain.usecase.ScanAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ScanProgressState(
    val isScanning: Boolean = false,
    val progressPercent: Float = 0f,
    val currentStepText: String = "",
    val scannedAppsCount: Int = 0
)

data class HomeUiState(
    val isLoading: Boolean = false,
    val scanProgress: ScanProgressState = ScanProgressState(),
    val assessment: RiskAssessment? = null,
    val riskyApps: List<InstalledApp> = emptyList(),
    val sensorStatuses: List<SensorStatus> = emptyList(),
    val recentAlerts: List<PrivacyEvent> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: InstalledAppRepository,
    private val riskRepository: RiskRepository,
    private val sensorRepository: SensorRepository,
    private val scanAppsUseCase: ScanAppsUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _scanProgress = MutableStateFlow(ScanProgressState())

    val uiState: StateFlow<HomeUiState> = combine(
        _scanProgress,
        appRepository.observeInstalledApps(),
        sensorRepository.observeSensorStatuses(),
        riskRepository.observeEvents()
    ) { progress, apps, sensors, events ->
        val assessment = calculateScoreUseCase(apps)
        HomeUiState(
            isLoading = progress.isScanning,
            scanProgress = progress,
            assessment = assessment,
            riskyApps = assessment.topRiskyApps,
            sensorStatuses = sensors,
            recentAlerts = events.take(3)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState(isLoading = false)
    )

    init {
        loadInitialDataIfEmpty()
    }

    private fun loadInitialDataIfEmpty() {
        viewModelScope.launch(dispatchers.io) {
            val existingApps = appRepository.observeInstalledApps().firstOrNull()
            if (existingApps.isNullOrEmpty()) {
                val scanned = scanAppsUseCase()
                val assessment = calculateScoreUseCase(scanned)
                riskRepository.saveAssessment(assessment)
            }
        }
    }

    fun runFullScan() {
        viewModelScope.launch {
            _scanProgress.value = ScanProgressState(
                isScanning = true,
                progressPercent = 0.15f,
                currentStepText = "ऐप्स लोड हो रहे हैं…",
                scannedAppsCount = 0
            )

            withContext(dispatchers.io) {
                delay(300)
                val scanned = scanAppsUseCase()

                _scanProgress.value = ScanProgressState(
                    isScanning = true,
                    progressPercent = 0.55f,
                    currentStepText = "अनुमतियों और जोखिम का विश्लेषण हो रहा है…",
                    scannedAppsCount = scanned.size
                )
                delay(400)

                val assessment = calculateScoreUseCase(scanned)

                _scanProgress.value = ScanProgressState(
                    isScanning = true,
                    progressPercent = 0.85f,
                    currentStepText = "प्राइवेसी परिणाम सहेजे जा रहे हैं…",
                    scannedAppsCount = scanned.size
                )
                delay(300)

                riskRepository.saveAssessment(assessment)

                _scanProgress.value = ScanProgressState(
                    isScanning = true,
                    progressPercent = 1.0f,
                    currentStepText = "स्कैन पूरा हुआ!",
                    scannedAppsCount = scanned.size
                )
                delay(400)
            }

            _scanProgress.value = ScanProgressState(isScanning = false)
        }
    }
}
