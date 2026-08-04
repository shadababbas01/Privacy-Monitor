package com.privacymonitor.android.data.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.privacymonitor.android.worker.DailySnapshotWorker

class PackageChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action == Intent.ACTION_PACKAGE_ADDED || action == Intent.ACTION_PACKAGE_REMOVED || action == Intent.ACTION_PACKAGE_REPLACED) {
            val workRequest = OneTimeWorkRequestBuilder<DailySnapshotWorker>().build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
