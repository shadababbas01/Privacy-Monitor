package com.privacymonitor.android.domain.usecase

import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import javax.inject.Inject

class ScanAppsUseCase @Inject constructor(
    private val appRepository: InstalledAppRepository
) {
    suspend operator fun invoke(): List<InstalledApp> {
        return appRepository.refreshAppsScan()
    }
}
