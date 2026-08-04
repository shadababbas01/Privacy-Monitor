package com.privacymonitor.android.data.repository

import com.privacymonitor.android.data.local.db.AppDao
import com.privacymonitor.android.data.local.db.InstalledAppEntity
import com.privacymonitor.android.data.system.AndroidPackageManager
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.PermissionInfo
import com.privacymonitor.android.domain.model.RiskLevel
import com.privacymonitor.android.domain.model.RiskReason
import com.privacymonitor.android.domain.repository.InstalledAppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val packageManager: AndroidPackageManager
) : InstalledAppRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getInstalledApps(): List<InstalledApp> {
        val entities = appDao.getAllApps()
        if (entities.isEmpty()) {
            return refreshAppsScan()
        }
        return entities.map { mapEntityToModel(it) }
    }

    override fun observeInstalledApps(): Flow<List<InstalledApp>> {
        return appDao.observeAllApps().map { list ->
            list.map { mapEntityToModel(it) }
        }
    }

    override suspend fun getAppDetails(packageName: String): InstalledApp? {
        val entity = appDao.getAppByPackage(packageName) ?: return null
        return mapEntityToModel(entity)
    }

    override suspend fun refreshAppsScan(): List<InstalledApp> {
        val scannedApps = packageManager.scanAllApps()
        val entities = scannedApps.map { mapModelToEntity(it) }
        appDao.insertApps(entities)
        appDao.deleteRemovedApps(scannedApps.map { it.packageName })
        return scannedApps
    }

    private fun mapEntityToModel(entity: InstalledAppEntity): InstalledApp {
        val permissions: List<PermissionInfo> = try {
            json.decodeFromString(entity.permissionsJson)
        } catch (e: Exception) { emptyList() }

        val specialAccess: List<String> = try {
            json.decodeFromString(entity.specialAccessJson)
        } catch (e: Exception) { emptyList() }

        val reasons: List<RiskReason> = try {
            json.decodeFromString(entity.riskReasonsJson)
        } catch (e: Exception) { emptyList() }

        return InstalledApp(
            packageName = entity.packageName,
            appName = entity.appName,
            versionName = entity.versionName,
            isSystemApp = entity.isSystemApp,
            installerPackageName = entity.installerPackageName,
            firstInstallTime = entity.firstInstallTime,
            lastUpdateTime = entity.lastUpdateTime,
            permissions = permissions,
            specialAccessList = specialAccess,
            riskScore = entity.riskScore,
            riskLevel = try { RiskLevel.valueOf(entity.riskLevel) } catch (e: Exception) { RiskLevel.SAFE },
            riskReasons = reasons,
            totalDataUsageBytes = entity.totalDataUsageBytes,
            isUpiOrFinancial = entity.isUpiOrFinancial
        )
    }

    private fun mapModelToEntity(model: InstalledApp): InstalledAppEntity {
        return InstalledAppEntity(
            packageName = model.packageName,
            appName = model.appName,
            versionName = model.versionName,
            isSystemApp = model.isSystemApp,
            installerPackageName = model.installerPackageName,
            firstInstallTime = model.firstInstallTime,
            lastUpdateTime = model.lastUpdateTime,
            permissionsJson = json.encodeToString(model.permissions),
            specialAccessJson = json.encodeToString(model.specialAccessList),
            riskScore = model.riskScore,
            riskLevel = model.riskLevel.name,
            riskReasonsJson = json.encodeToString(model.riskReasons),
            totalDataUsageBytes = model.totalDataUsageBytes,
            isUpiOrFinancial = model.isUpiOrFinancial
        )
    }
}
