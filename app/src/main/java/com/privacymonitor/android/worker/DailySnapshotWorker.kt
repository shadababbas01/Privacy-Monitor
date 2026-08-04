package com.privacymonitor.android.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import com.privacymonitor.android.domain.repository.RiskRepository
import com.privacymonitor.android.domain.usecase.CalculateScoreUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailySnapshotWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val appRepository: InstalledAppRepository,
    private val riskRepository: RiskRepository,
    private val calculateScoreUseCase: CalculateScoreUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val apps = appRepository.refreshAppsScan()
            val assessment = calculateScoreUseCase(apps)
            riskRepository.saveAssessment(assessment)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
