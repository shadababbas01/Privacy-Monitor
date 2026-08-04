package com.privacymonitor.android.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM weekly_reports ORDER BY weekEndDate DESC")
    fun observeAllReports(): Flow<List<WeeklyReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: WeeklyReportEntity)
}
