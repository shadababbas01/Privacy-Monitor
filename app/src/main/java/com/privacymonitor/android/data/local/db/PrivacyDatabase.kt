package com.privacymonitor.android.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        InstalledAppEntity::class,
        PrivacyEventEntity::class,
        ScoreSnapshotEntity::class,
        WeeklyReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrivacyDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun eventDao(): EventDao
    abstract fun scoreDao(): ScoreDao
    abstract fun reportDao(): ReportDao
}
