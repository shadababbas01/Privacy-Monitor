package com.privacymonitor.android.domain.repository

import com.privacymonitor.android.domain.model.WeeklyReport
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ReportRepository {
    fun observeWeeklyReports(): Flow<List<WeeklyReport>>
    suspend fun generateCurrentWeeklyReport(): WeeklyReport
    suspend fun exportReportAsPdf(report: WeeklyReport): File
}
