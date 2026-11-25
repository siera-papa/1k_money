package com.project1k.money.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class BackupWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = MoneyDatabase.get(applicationContext)
        val manager = BackupManager(applicationContext, database)
        if (manager.backupFolderUri() == null) return Result.success()
        return when (manager.createAutomaticBackup()) {
            is BackupWriteResult.Success -> Result.success()
            is BackupWriteResult.Error -> Result.retry()
        }
    }
}

object BackupScheduler {
    private const val UNIQUE_WORK = "project1k_money_daily_backup"

    fun ensureScheduled(context: Context) {
        val manager = BackupManager(context.applicationContext, MoneyDatabase.get(context))
        if (manager.backupFolderUri() == null) return
        val request = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
