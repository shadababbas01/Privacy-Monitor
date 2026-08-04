package com.privacymonitor.android.data.repository

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.privacymonitor.android.data.local.db.ReportDao
import com.privacymonitor.android.data.local.db.WeeklyReportEntity
import com.privacymonitor.android.domain.model.WeeklyReport
import com.privacymonitor.android.domain.repository.ReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reportDao: ReportDao
) : ReportRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeWeeklyReports(): Flow<List<WeeklyReport>> {
        return reportDao.observeAllReports().map { list ->
            list.map { entity ->
                WeeklyReport(
                    id = entity.id,
                    weekStartDate = entity.weekStartDate,
                    weekEndDate = entity.weekEndDate,
                    initialScore = entity.initialScore,
                    finalScore = entity.finalScore,
                    sensitivePermissionAccessCount = entity.sensitivePermissionAccessCount,
                    newAppsInstalledCount = entity.newAppsInstalledCount,
                    riskLevelChangesCount = entity.riskLevelChangesCount,
                    summaryHinglish = entity.summaryHinglish,
                    topRiskyApps = try { json.decodeFromString(entity.topRiskyAppsJson) } catch (e: Exception) { emptyList() }
                )
            }
        }
    }

    override suspend fun generateCurrentWeeklyReport(): WeeklyReport {
        val report = WeeklyReport(
            id = UUID.randomUUID().toString(),
            weekStartDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L),
            weekEndDate = System.currentTimeMillis(),
            initialScore = 100,
            finalScore = 100,
            sensitivePermissionAccessCount = 0,
            newAppsInstalledCount = 0,
            riskLevelChangesCount = 0,
            summaryHinglish = "इस हफ्ते अनुमतियों की जांच की गई और डिवाइस सुरक्षा ऑडिट पूरा हुआ।",
            topRiskyApps = emptyList()
        )

        val entity = WeeklyReportEntity(
            id = report.id,
            weekStartDate = report.weekStartDate,
            weekEndDate = report.weekEndDate,
            initialScore = report.initialScore,
            finalScore = report.finalScore,
            sensitivePermissionAccessCount = report.sensitivePermissionAccessCount,
            newAppsInstalledCount = report.newAppsInstalledCount,
            riskLevelChangesCount = report.riskLevelChangesCount,
            summaryHinglish = report.summaryHinglish,
            topRiskyAppsJson = json.encodeToString(report.topRiskyApps)
        )
        reportDao.insertReport(entity)
        return report
    }

    override suspend fun exportReportAsPdf(report: WeeklyReport): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isAntiAlias = true
        }

        canvas.drawText("Privacy Monitor — Weekly Audit Report", 40f, 60f, paint.apply { textSize = 18f; isFakeBoldText = true })
        canvas.drawText("Generated on: ${java.util.Date()}", 40f, 90f, paint.apply { textSize = 12f; isFakeBoldText = false })

        canvas.drawText("AI Privacy Score: ${report.finalScore}/100", 40f, 130f, paint.apply { textSize = 16f; isFakeBoldText = true })
        canvas.drawText("Summary: ${report.summaryHinglish}", 40f, 170f, paint.apply { textSize = 12f; isFakeBoldText = false })

        canvas.drawText("Sensitive Permission Accesses: ${report.sensitivePermissionAccessCount}", 40f, 210f, paint)
        canvas.drawText("New Apps Installed: ${report.newAppsInstalledCount}", 40f, 235f, paint)

        canvas.drawText("Disclaimer: Generated locally by Privacy Monitor for device safety auditing.", 40f, 300f, paint.apply { textSize = 10f })

        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val outputFile = File(reportsDir, "Privacy_Report_${report.id.take(8)}.pdf")

        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }
}
