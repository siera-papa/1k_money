package com.project1k.money.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MoneyDatabase_Impl : MoneyDatabase() {
  private val _moneyDao: Lazy<MoneyDao> = lazy {
    MoneyDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(5, "83e7b7e9478690ef4e0c49a34a21d61e", "2219e2640ce4a003977132baafe55c0a") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `currency` TEXT NOT NULL, `openingBalanceAtomic` INTEGER NOT NULL, `openingDate` INTEGER NOT NULL, `groupLabel` TEXT, `trackerId` TEXT, `archived` INTEGER NOT NULL, `type` TEXT NOT NULL DEFAULT 'primary')")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `income_sources` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `currency` TEXT NOT NULL, `archived` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `parentId` INTEGER, `pinned` INTEGER NOT NULL, `archived` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_parentId` ON `categories` (`parentId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tags` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `pinned` INTEGER NOT NULL, `archived` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `categoryId` INTEGER, `incomeSourceId` INTEGER, `incomeSourceAmountAtomic` INTEGER, `note` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_categoryId` ON `transactions` (`categoryId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_incomeSourceId` ON `transactions` (`incomeSourceId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_timestamp` ON `transactions` (`timestamp`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transaction_legs` (`transactionId` INTEGER NOT NULL, `accountId` INTEGER NOT NULL, `amountAtomic` INTEGER NOT NULL, PRIMARY KEY(`transactionId`, `accountId`), FOREIGN KEY(`transactionId`) REFERENCES `transactions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `accounts`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_legs_transactionId` ON `transaction_legs` (`transactionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_legs_accountId` ON `transaction_legs` (`accountId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transaction_tags` (`transactionId` INTEGER NOT NULL, `tagId` INTEGER NOT NULL, PRIMARY KEY(`transactionId`, `tagId`), FOREIGN KEY(`transactionId`) REFERENCES `transactions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`tagId`) REFERENCES `tags`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_tags_transactionId` ON `transaction_tags` (`transactionId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_tags_tagId` ON `transaction_tags` (`tagId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '83e7b7e9478690ef4e0c49a34a21d61e')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `accounts`")
        connection.execSQL("DROP TABLE IF EXISTS `income_sources`")
        connection.execSQL("DROP TABLE IF EXISTS `categories`")
        connection.execSQL("DROP TABLE IF EXISTS `tags`")
        connection.execSQL("DROP TABLE IF EXISTS `transactions`")
        connection.execSQL("DROP TABLE IF EXISTS `transaction_legs`")
        connection.execSQL("DROP TABLE IF EXISTS `transaction_tags`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsAccounts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAccounts.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("currency", TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("openingBalanceAtomic", TableInfo.Column("openingBalanceAtomic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("openingDate", TableInfo.Column("openingDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("groupLabel", TableInfo.Column("groupLabel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("trackerId", TableInfo.Column("trackerId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("archived", TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAccounts.put("type", TableInfo.Column("type", "TEXT", true, 0, "'primary'", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAccounts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAccounts: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAccounts: TableInfo = TableInfo("accounts", _columnsAccounts, _foreignKeysAccounts, _indicesAccounts)
        val _existingAccounts: TableInfo = read(connection, "accounts")
        if (!_infoAccounts.equals(_existingAccounts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |accounts(com.project1k.money.data.AccountEntity).
              | Expected:
              |""".trimMargin() + _infoAccounts + """
              |
              | Found:
              |""".trimMargin() + _existingAccounts)
        }
        val _columnsIncomeSources: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsIncomeSources.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsIncomeSources.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsIncomeSources.put("currency", TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsIncomeSources.put("archived", TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysIncomeSources: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesIncomeSources: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoIncomeSources: TableInfo = TableInfo("income_sources", _columnsIncomeSources, _foreignKeysIncomeSources, _indicesIncomeSources)
        val _existingIncomeSources: TableInfo = read(connection, "income_sources")
        if (!_infoIncomeSources.equals(_existingIncomeSources)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |income_sources(com.project1k.money.data.IncomeSourceEntity).
              | Expected:
              |""".trimMargin() + _infoIncomeSources + """
              |
              | Found:
              |""".trimMargin() + _existingIncomeSources)
        }
        val _columnsCategories: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCategories.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("parentId", TableInfo.Column("parentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("pinned", TableInfo.Column("pinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("archived", TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCategories: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCategories: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCategories.add(TableInfo.Index("index_categories_parentId", false, listOf("parentId"), listOf("ASC")))
        val _infoCategories: TableInfo = TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories)
        val _existingCategories: TableInfo = read(connection, "categories")
        if (!_infoCategories.equals(_existingCategories)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |categories(com.project1k.money.data.CategoryEntity).
              | Expected:
              |""".trimMargin() + _infoCategories + """
              |
              | Found:
              |""".trimMargin() + _existingCategories)
        }
        val _columnsTags: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTags.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("pinned", TableInfo.Column("pinned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("archived", TableInfo.Column("archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTags: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTags: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTags: TableInfo = TableInfo("tags", _columnsTags, _foreignKeysTags, _indicesTags)
        val _existingTags: TableInfo = read(connection, "tags")
        if (!_infoTags.equals(_existingTags)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tags(com.project1k.money.data.TagEntity).
              | Expected:
              |""".trimMargin() + _infoTags + """
              |
              | Found:
              |""".trimMargin() + _existingTags)
        }
        val _columnsTransactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("type", TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("categoryId", TableInfo.Column("categoryId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("incomeSourceId", TableInfo.Column("incomeSourceId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("incomeSourceAmountAtomic", TableInfo.Column("incomeSourceAmountAtomic", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("note", TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTransactions.add(TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION", listOf("categoryId"), listOf("id")))
        val _indicesTransactions: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTransactions.add(TableInfo.Index("index_transactions_categoryId", false, listOf("categoryId"), listOf("ASC")))
        _indicesTransactions.add(TableInfo.Index("index_transactions_incomeSourceId", false, listOf("incomeSourceId"), listOf("ASC")))
        _indicesTransactions.add(TableInfo.Index("index_transactions_timestamp", false, listOf("timestamp"), listOf("ASC")))
        val _infoTransactions: TableInfo = TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions)
        val _existingTransactions: TableInfo = read(connection, "transactions")
        if (!_infoTransactions.equals(_existingTransactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transactions(com.project1k.money.data.TransactionEntity).
              | Expected:
              |""".trimMargin() + _infoTransactions + """
              |
              | Found:
              |""".trimMargin() + _existingTransactions)
        }
        val _columnsTransactionLegs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactionLegs.put("transactionId", TableInfo.Column("transactionId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactionLegs.put("accountId", TableInfo.Column("accountId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactionLegs.put("amountAtomic", TableInfo.Column("amountAtomic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactionLegs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTransactionLegs.add(TableInfo.ForeignKey("transactions", "CASCADE", "NO ACTION", listOf("transactionId"), listOf("id")))
        _foreignKeysTransactionLegs.add(TableInfo.ForeignKey("accounts", "RESTRICT", "NO ACTION", listOf("accountId"), listOf("id")))
        val _indicesTransactionLegs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTransactionLegs.add(TableInfo.Index("index_transaction_legs_transactionId", false, listOf("transactionId"), listOf("ASC")))
        _indicesTransactionLegs.add(TableInfo.Index("index_transaction_legs_accountId", false, listOf("accountId"), listOf("ASC")))
        val _infoTransactionLegs: TableInfo = TableInfo("transaction_legs", _columnsTransactionLegs, _foreignKeysTransactionLegs, _indicesTransactionLegs)
        val _existingTransactionLegs: TableInfo = read(connection, "transaction_legs")
        if (!_infoTransactionLegs.equals(_existingTransactionLegs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transaction_legs(com.project1k.money.data.TransactionLegEntity).
              | Expected:
              |""".trimMargin() + _infoTransactionLegs + """
              |
              | Found:
              |""".trimMargin() + _existingTransactionLegs)
        }
        val _columnsTransactionTags: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactionTags.put("transactionId", TableInfo.Column("transactionId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactionTags.put("tagId", TableInfo.Column("tagId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactionTags: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysTransactionTags.add(TableInfo.ForeignKey("transactions", "CASCADE", "NO ACTION", listOf("transactionId"), listOf("id")))
        _foreignKeysTransactionTags.add(TableInfo.ForeignKey("tags", "CASCADE", "NO ACTION", listOf("tagId"), listOf("id")))
        val _indicesTransactionTags: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTransactionTags.add(TableInfo.Index("index_transaction_tags_transactionId", false, listOf("transactionId"), listOf("ASC")))
        _indicesTransactionTags.add(TableInfo.Index("index_transaction_tags_tagId", false, listOf("tagId"), listOf("ASC")))
        val _infoTransactionTags: TableInfo = TableInfo("transaction_tags", _columnsTransactionTags, _foreignKeysTransactionTags, _indicesTransactionTags)
        val _existingTransactionTags: TableInfo = read(connection, "transaction_tags")
        if (!_infoTransactionTags.equals(_existingTransactionTags)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transaction_tags(com.project1k.money.data.TransactionTagEntity).
              | Expected:
              |""".trimMargin() + _infoTransactionTags + """
              |
              | Found:
              |""".trimMargin() + _existingTransactionTags)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "accounts", "income_sources", "categories", "tags", "transactions", "transaction_legs", "transaction_tags")
  }

  public override fun clearAllTables() {
    super.performClear(true, "accounts", "income_sources", "categories", "tags", "transactions", "transaction_legs", "transaction_tags")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(MoneyDao::class, MoneyDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun dao(): MoneyDao = _moneyDao.value
}
