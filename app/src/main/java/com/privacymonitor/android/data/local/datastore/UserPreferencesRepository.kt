package com.privacymonitor.android.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_privacy_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKeys {
        val KEY_THEME = stringPreferencesKey("theme_mode")
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_RETENTION_DAYS = intPreferencesKey("retention_days")
        val KEY_CLOUD_AI_OPT_IN = booleanPreferencesKey("cloud_ai_opt_in")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_THEME] ?: "SYSTEM"
    }

    val appLanguage: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_LANGUAGE] ?: "hi"
    }

    val retentionDays: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_RETENTION_DAYS] ?: 30
    }

    val cloudAiOptIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_CLOUD_AI_OPT_IN] ?: false
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_THEME] = mode
        }
    }

    suspend fun setAppLanguage(lang: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_LANGUAGE] = lang
        }
    }

    suspend fun setRetentionDays(days: Int) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_RETENTION_DAYS] = days
        }
    }

    suspend fun setCloudAiOptIn(optIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_CLOUD_AI_OPT_IN] = optIn
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_ONBOARDING_COMPLETED] = completed
        }
    }
}
