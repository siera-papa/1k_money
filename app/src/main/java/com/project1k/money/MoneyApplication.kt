package com.project1k.money

import android.app.Application
import com.project1k.money.calc.CashCalcStore
import com.project1k.money.data.BackupScheduler
import com.project1k.money.data.MoneyDatabase
import com.project1k.money.data.MoneyRepository

class MoneyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BackupScheduler.ensureScheduled(this)
    }

    val database by lazy { MoneyDatabase.get(this) }
    val repository by lazy { MoneyRepository(database) }
    val cashCalcStore by lazy { CashCalcStore(this) }
}
