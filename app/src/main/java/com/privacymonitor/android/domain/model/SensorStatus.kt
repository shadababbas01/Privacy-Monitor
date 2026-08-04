package com.privacymonitor.android.domain.model

enum class SensorState {
    ACTIVE,
    RECENTLY_USED,
    OFF,
    UNAVAILABLE
}

data class SensorStatus(
    val id: String,
    val name: String,
    val state: SensorState,
    val appsWithAccessCount: Int,
    val lastActiveTimestamp: Long? = null,
    val limitationNote: String? = null
)
