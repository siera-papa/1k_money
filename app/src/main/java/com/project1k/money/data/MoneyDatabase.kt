package com.project1k.money.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        AccountEntity::class,
        IncomeSourceEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        TransactionEntity::class,
        TransactionLegEntity::class,
        TransactionTagEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class MoneyDatabase : RoomDatabase() {
    abstract fun dao(): MoneyDao

    companion object {
        @Volatile private var INSTANCE: MoneyDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE categories ADD COLUMN parentId INTEGER")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_categories_parentId ON categories(parentId)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS income_sources (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        currency TEXT NOT NULL,
                        archived INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("ALTER TABLE transactions ADD COLUMN incomeSourceId INTEGER")
                db.execSQL("ALTER TABLE transactions ADD COLUMN incomeSourceAmountAtomic INTEGER")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_transactions_incomeSourceId ON transactions(incomeSourceId)")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE accounts ADD COLUMN trackerId TEXT")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE accounts ADD COLUMN type TEXT NOT NULL DEFAULT 'primary'")
            }
        }

        fun get(context: Context): MoneyDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MoneyDatabase::class.java,
                "project1k_money.db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
                .also { INSTANCE = it }
        }
    }
}
