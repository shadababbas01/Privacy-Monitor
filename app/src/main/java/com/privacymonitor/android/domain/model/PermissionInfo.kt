package com.privacymonitor.android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PermissionInfo(
    val name: String,
    val isGranted: Boolean,
    val isSensitive: Boolean,
    val category: String,
    val description: String,
    val lastUsedTimestamp: Long? = null
)
