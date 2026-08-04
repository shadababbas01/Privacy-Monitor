package com.privacymonitor.android.di

import com.privacymonitor.android.data.local.advisor.LocalPrivacyAdvisor
import com.privacymonitor.android.data.repository.AppRepositoryImpl
import com.privacymonitor.android.data.repository.ReportRepositoryImpl
import com.privacymonitor.android.data.repository.RiskRepositoryImpl
import com.privacymonitor.android.data.repository.SensorRepositoryImpl
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import com.privacymonitor.android.domain.repository.PrivacyAdvisor
import com.privacymonitor.android.domain.repository.ReportRepository
import com.privacymonitor.android.domain.repository.RiskRepository
import com.privacymonitor.android.domain.repository.SensorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInstalledAppRepository(impl: AppRepositoryImpl): InstalledAppRepository

    @Binds
    @Singleton
    abstract fun bindRiskRepository(impl: RiskRepositoryImpl): RiskRepository

    @Binds
    @Singleton
    abstract fun bindSensorRepository(impl: SensorRepositoryImpl): SensorRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(impl: ReportRepositoryImpl): ReportRepository

    @Binds
    @Singleton
    abstract fun bindPrivacyAdvisor(impl: LocalPrivacyAdvisor): PrivacyAdvisor
}
