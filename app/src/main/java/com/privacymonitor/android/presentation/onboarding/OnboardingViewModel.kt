package com.privacymonitor.android.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.core.util.DispatcherProvider
import com.privacymonitor.android.data.local.datastore.UserPreferencesRepository
import com.privacymonitor.android.domain.usecase.ScanAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val scanAppsUseCase: ScanAppsUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    fun completeOnboarding(onFinished: () -> Unit) {
        viewModelScope.launch {
            _isScanning.value = true
            withContext(dispatchers.io) {
                scanAppsUseCase()
                userPreferencesRepository.setOnboardingCompleted(true)
            }
            _isScanning.value = false
            onFinished()
        }
    }
}
