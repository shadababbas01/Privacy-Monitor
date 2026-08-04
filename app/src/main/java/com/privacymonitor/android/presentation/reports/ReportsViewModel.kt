package com.privacymonitor.android.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.privacymonitor.android.domain.model.WeeklyReport
import com.privacymonitor.android.domain.repository.ReportRepository
import com.privacymonitor.android.domain.usecase.GeneratePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val generatePdfUseCase: GeneratePdfUseCase
) : ViewModel() {

    private val _reports = MutableStateFlow<List<WeeklyReport>>(emptyList())
    val reports: StateFlow<List<WeeklyReport>> = _reports

    private val _exportedPdf = MutableStateFlow<File?>(null)
    val exportedPdf: StateFlow<File?> = _exportedPdf

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            val report = reportRepository.generateCurrentWeeklyReport()
            _reports.value = listOf(report)
        }
    }

    fun exportPdf(report: WeeklyReport) {
        viewModelScope.launch {
            val file = generatePdfUseCase(report)
            _exportedPdf.value = file
        }
    }
}
