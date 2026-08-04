package com.privacymonitor.android.di

import android.content.Context
import androidx.room.Room
import com.privacymonitor.android.data.local.db.AppDao
import com.privacymonitor.android.data.local.db.EventDao
import com.privacymonitor.android.data.local.db.PrivacyDatabase
import com.privacymonitor.android.data.local.db.ReportDao
import com.privacymonitor.android.data.local.db.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePrivacyDatabase(@ApplicationContext context: Context): PrivacyDatabase {
        return Room.databaseBuilder(
            context,
            PrivacyDatabase::class.java,
            "privacy_monitor_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAppDao(db: PrivacyDatabase): AppDao = db.appDao()

    @Provides
    fun provideEventDao(db: PrivacyDatabase): EventDao = db.eventDao()

    @Provides
    fun provideScoreDao(db: PrivacyDatabase): ScoreDao = db.scoreDao()

    @Provides
    fun provideReportDao(db: PrivacyDatabase): ReportDao = db.reportDao()
}
