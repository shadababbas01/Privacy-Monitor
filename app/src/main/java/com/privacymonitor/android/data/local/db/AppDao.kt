package com.privacymonitor.android.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM installed_apps ORDER BY riskScore ASC")
    fun observeAllApps(): Flow<List<InstalledAppEntity>>

    @Query("SELECT * FROM installed_apps ORDER BY riskScore ASC")
    suspend fun getAllApps(): List<InstalledAppEntity>

    @Query("SELECT * FROM installed_apps WHERE packageName = :packageName LIMIT 1")
    suspend fun getAppByPackage(packageName: String): InstalledAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<InstalledAppEntity>)

    @Query("DELETE FROM installed_apps WHERE packageName NOT IN (:currentPackageNames)")
    suspend fun deleteRemovedApps(currentPackageNames: List<String>)
}
