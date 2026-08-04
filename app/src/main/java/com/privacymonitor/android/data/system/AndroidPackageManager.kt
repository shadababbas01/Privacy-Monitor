package com.privacymonitor.android.data.system

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.privacymonitor.android.domain.engine.RiskEngine
import com.privacymonitor.android.domain.model.InstalledApp
import com.privacymonitor.android.domain.model.PermissionInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPackageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val riskEngine: RiskEngine
) {

    fun scanAllApps(): List<InstalledApp> {
        val pm = context.packageManager

        // Query packages with launchable App Drawer icons
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val launcherPackages: Set<String> = try {
            pm.queryIntentActivities(mainIntent, 0)
                .mapNotNull { it.activityInfo?.packageName }
                .toSet()
        } catch (e: Exception) {
            emptySet()
        }

        val packages: List<PackageInfo> = try {
            pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        } catch (e: Exception) {
            emptyList()
        }

        return packages.mapNotNull { packageInfo ->
            val appInfo = packageInfo.applicationInfo ?: return@mapNotNull null
            val packageName = packageInfo.packageName

            // Skip self package from risk scoring
            if (packageName == context.packageName) return@mapNotNull null

            // App Drawer Filter: App must have a launcher icon or launch intent
            val isAppDrawerApp = launcherPackages.contains(packageName) || pm.getLaunchIntentForPackage(packageName) != null
            if (!isAppDrawerApp) {
                return@mapNotNull null
            }

            val appName = pm.getApplicationLabel(appInfo).toString()
            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

            val installer = try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    pm.getInstallSourceInfo(packageName).installingPackageName
                } else {
                    @Suppress("DEPRECATION")
                    pm.getInstallerPackageName(packageName)
                }
            } catch (e: Exception) {
                null
            }

            val requestedPermissions = packageInfo.requestedPermissions ?: emptyArray()
            val permissionFlags = packageInfo.requestedPermissionsFlags ?: intArrayOf()

            val permissions = requestedPermissions.mapIndexed { index, permName ->
                val isGranted = if (index < permissionFlags.size) {
                    (permissionFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
                } else false

                PermissionInfo(
                    name = permName,
                    isGranted = isGranted,
                    isSensitive = isSensitivePermission(permName),
                    category = getPermissionCategory(permName),
                    description = getPermissionDescription(permName)
                )
            }

            val specialAccess = checkSpecialAccess(packageName, requestedPermissions)

            val eval = riskEngine.evaluateApp(
                packageName = packageName,
                appName = appName,
                isSystemApp = isSystemApp,
                installerPackage = installer,
                permissions = permissions,
                specialAccessList = specialAccess
            )

            val isUpi = isFinancialPackage(packageName, appName)

            InstalledApp(
                packageName = packageName,
                appName = appName,
                versionName = packageInfo.versionName ?: "1.0",
                isSystemApp = isSystemApp,
                installerPackageName = installer,
                firstInstallTime = packageInfo.firstInstallTime,
                lastUpdateTime = packageInfo.lastUpdateTime,
                permissions = permissions,
                specialAccessList = specialAccess,
                riskScore = eval.appScore,
                riskLevel = eval.riskLevel,
                riskReasons = eval.reasons,
                totalDataUsageBytes = 0L,
                isUpiOrFinancial = isUpi
            )
        }
    }

    private fun isSensitivePermission(permName: String): Boolean {
        val upper = permName.uppercase()
        return upper.contains("CAMERA") ||
                upper.contains("RECORD_AUDIO") ||
                upper.contains("LOCATION") ||
                upper.contains("CONTACTS") ||
                upper.contains("SMS") ||
                upper.contains("CALL_LOG")
    }

    private fun getPermissionCategory(permName: String): String {
        val upper = permName.uppercase()
        return when {
            upper.contains("LOCATION") -> "Location"
            upper.contains("CAMERA") -> "Camera"
            upper.contains("AUDIO") || upper.contains("MICROPHONE") -> "Microphone"
            upper.contains("CONTACTS") -> "Contacts"
            upper.contains("SMS") -> "SMS"
            upper.contains("CALL_LOG") -> "Call Log"
            upper.contains("STORAGE") -> "Storage"
            else -> "Other"
        }
    }

    private fun getPermissionDescription(permName: String): String {
        return "Allows access to $permName."
    }

    private fun checkSpecialAccess(packageName: String, requestedPermissions: Array<String>): List<String> {
        val list = mutableListOf<String>()
        if (requestedPermissions.contains("android.permission.BIND_ACCESSIBILITY_SERVICE")) {
            list.add("ACCESSIBILITY_SERVICE")
        }
        if (requestedPermissions.contains("android.permission.SYSTEM_ALERT_WINDOW")) {
            list.add("SYSTEM_ALERT_WINDOW")
        }
        if (requestedPermissions.contains("android.permission.PACKAGE_USAGE_STATS")) {
            list.add("PACKAGE_USAGE_STATS")
        }
        if (requestedPermissions.contains("android.permission.BIND_NOTIFICATION_LISTENER_SERVICE")) {
            list.add("NOTIFICATION_LISTENER")
        }
        return list
    }

    private fun isFinancialPackage(packageName: String, appName: String): Boolean {
        val lowerPkg = packageName.lowercase()
        val lowerName = appName.lowercase()
        return lowerPkg.contains("paytm") || lowerPkg.contains("phonepe") ||
                lowerPkg.contains("gpay") || lowerPkg.contains("upi") ||
                lowerPkg.contains("bank") || lowerName.contains("pay") ||
                lowerName.contains("bank") || lowerName.contains("upi")
    }
}
