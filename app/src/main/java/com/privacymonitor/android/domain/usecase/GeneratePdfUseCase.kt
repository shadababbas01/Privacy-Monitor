package com.privacymonitor.android.domain.usecase

import com.privacymonitor.android.domain.model.WeeklyReport
import com.privacymonitor.android.domain.repository.ReportRepository
import java.io.File
import javax.inject.Inject

class GeneratePdfUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(report: WeeklyReport): File {
        return reportRepository.exportReportAsPdf(report)
    }
}
