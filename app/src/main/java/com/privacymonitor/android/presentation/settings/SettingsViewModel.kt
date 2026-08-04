package com.privacymonitor.android.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.data.local.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: String = "SYSTEM",
    val language: String = "hi",
    val retentionDays: Int = 30,
    val cloudAiOptIn: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        userPreferencesRepository.themeMode,
        userPreferencesRepository.appLanguage,
        userPreferencesRepository.retentionDays,
        userPreferencesRepository.cloudAiOptIn
    ) { theme, lang, retention, cloudAi ->
        SettingsUiState(
            themeMode = theme,
            language = lang,
            retentionDays = retention,
            cloudAiOptIn = cloudAi
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SettingsUiState()
    )

    fun setThemeMode(mode: String) {
        viewModelScope.launch { userPreferencesRepository.setThemeMode(mode) }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch { userPreferencesRepository.setAppLanguage(lang) }
    }

    fun setRetentionDays(days: Int) {
        viewModelScope.launch { userPreferencesRepository.setRetentionDays(days) }
    }

    fun setCloudAiOptIn(optIn: Boolean) {
        viewModelScope.launch { userPreferencesRepository.setCloudAiOptIn(optIn) }
    }
}
