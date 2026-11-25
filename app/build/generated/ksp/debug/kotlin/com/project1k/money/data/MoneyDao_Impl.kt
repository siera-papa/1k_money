package com.project1k.money.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MoneyDao_Impl(
  __db: RoomDatabase,
) : MoneyDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAccountEntity: EntityInsertAdapter<AccountEntity>

  private val __insertAdapterOfIncomeSourceEntity: EntityInsertAdapter<IncomeSourceEntity>

  private val __insertAdapterOfCategoryEntity: EntityInsertAdapter<CategoryEntity>

  private val __insertAdapterOfTagEntity: EntityInsertAdapter<TagEntity>

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>

  private val __insertAdapterOfTransactionLegEntity: EntityInsertAdapter<TransactionLegEntity>

  private val __insertAdapterOfTransactionTagEntity: EntityInsertAdapter<TransactionTagEntity>

  private val __insertAdapterOfAccountEntity_1: EntityInsertAdapter<AccountEntity>

  private val __insertAdapterOfIncomeSourceEntity_1: EntityInsertAdapter<IncomeSourceEntity>

  private val __insertAdapterOfCategoryEntity_1: EntityInsertAdapter<CategoryEntity>

  private val __insertAdapterOfTagEntity_1: EntityInsertAdapter<TagEntity>

  private val __insertAdapterOfTransactionEntity_1: EntityInsertAdapter<TransactionEntity>

  private val __insertAdapterOfTransactionTagEntity_1: EntityInsertAdapter<TransactionTagEntity>

  private val __deleteAdapterOfTransactionEntity: EntityDeleteOrUpdateAdapter<TransactionEntity>

  private val __updateAdapterOfAccountEntity: EntityDeleteOrUpdateAdapter<AccountEntity>

  private val __updateAdapterOfIncomeSourceEntity: EntityDeleteOrUpdateAdapter<IncomeSourceEntity>

  private val __updateAdapterOfCategoryEntity: EntityDeleteOrUpdateAdapter<CategoryEntity>

  private val __updateAdapterOfTagEntity: EntityDeleteOrUpdateAdapter<TagEntity>

  private val __updateAdapterOfTransactionEntity: EntityDeleteOrUpdateAdapter<TransactionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAccountEntity = object : EntityInsertAdapter<AccountEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `accounts` (`id`,`name`,`currency`,`openingBalanceAtomic`,`openingDate`,`groupLabel`,`trackerId`,`archived`,`type`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AccountEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        statement.bindLong(4, entity.openingBalanceAtomic)
        statement.bindLong(5, entity.openingDate)
        val _tmpGroupLabel: String? = entity.groupLabel
        if (_tmpGroupLabel == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpGroupLabel)
        }
        val _tmpTrackerId: String? = entity.trackerId
        if (_tmpTrackerId == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpTrackerId)
        }
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindText(9, entity.type)
      }
    }
    this.__insertAdapterOfIncomeSourceEntity = object : EntityInsertAdapter<IncomeSourceEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `income_sources` (`id`,`name`,`currency`,`archived`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: IncomeSourceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__insertAdapterOfCategoryEntity = object : EntityInsertAdapter<CategoryEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `categories` (`id`,`name`,`parentId`,`pinned`,`archived`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpParentId: Long? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpParentId)
        }
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
      }
    }
    this.__insertAdapterOfTagEntity = object : EntityInsertAdapter<TagEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `tags` (`id`,`name`,`pinned`,`archived`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TagEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp_1.toLong())
      }
    }
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `transactions` (`id`,`type`,`timestamp`,`categoryId`,`incomeSourceId`,`incomeSourceAmountAtomic`,`note`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.type)
        statement.bindLong(3, entity.timestamp)
        val _tmpCategoryId: Long? = entity.categoryId
        if (_tmpCategoryId == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpCategoryId)
        }
        val _tmpIncomeSourceId: Long? = entity.incomeSourceId
        if (_tmpIncomeSourceId == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpIncomeSourceId)
        }
        val _tmpIncomeSourceAmountAtomic: Long? = entity.incomeSourceAmountAtomic
        if (_tmpIncomeSourceAmountAtomic == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpIncomeSourceAmountAtomic)
        }
        statement.bindText(7, entity.note)
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
      }
    }
    this.__insertAdapterOfTransactionLegEntity = object : EntityInsertAdapter<TransactionLegEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `transaction_legs` (`transactionId`,`accountId`,`amountAtomic`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionLegEntity) {
        statement.bindLong(1, entity.transactionId)
        statement.bindLong(2, entity.accountId)
        statement.bindLong(3, entity.amountAtomic)
      }
    }
    this.__insertAdapterOfTransactionTagEntity = object : EntityInsertAdapter<TransactionTagEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `transaction_tags` (`transactionId`,`tagId`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionTagEntity) {
        statement.bindLong(1, entity.transactionId)
        statement.bindLong(2, entity.tagId)
      }
    }
    this.__insertAdapterOfAccountEntity_1 = object : EntityInsertAdapter<AccountEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `accounts` (`id`,`name`,`currency`,`openingBalanceAtomic`,`openingDate`,`groupLabel`,`trackerId`,`archived`,`type`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AccountEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        statement.bindLong(4, entity.openingBalanceAtomic)
        statement.bindLong(5, entity.openingDate)
        val _tmpGroupLabel: String? = entity.groupLabel
        if (_tmpGroupLabel == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpGroupLabel)
        }
        val _tmpTrackerId: String? = entity.trackerId
        if (_tmpTrackerId == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpTrackerId)
        }
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindText(9, entity.type)
      }
    }
    this.__insertAdapterOfIncomeSourceEntity_1 = object : EntityInsertAdapter<IncomeSourceEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `income_sources` (`id`,`name`,`currency`,`archived`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: IncomeSourceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
    this.__insertAdapterOfCategoryEntity_1 = object : EntityInsertAdapter<CategoryEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `categories` (`id`,`name`,`parentId`,`pinned`,`archived`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpParentId: Long? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpParentId)
        }
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
      }
    }
    this.__insertAdapterOfTagEntity_1 = object : EntityInsertAdapter<TagEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tags` (`id`,`name`,`pinned`,`archived`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TagEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp_1.toLong())
      }
    }
    this.__insertAdapterOfTransactionEntity_1 = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`timestamp`,`categoryId`,`incomeSourceId`,`incomeSourceAmountAtomic`,`note`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.type)
        statement.bindLong(3, entity.timestamp)
        val _tmpCategoryId: Long? = entity.categoryId
        if (_tmpCategoryId == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpCategoryId)
        }
        val _tmpIncomeSourceId: Long? = entity.incomeSourceId
        if (_tmpIncomeSourceId == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpIncomeSourceId)
        }
        val _tmpIncomeSourceAmountAtomic: Long? = entity.incomeSourceAmountAtomic
        if (_tmpIncomeSourceAmountAtomic == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpIncomeSourceAmountAtomic)
        }
        statement.bindText(7, entity.note)
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
      }
    }
    this.__insertAdapterOfTransactionTagEntity_1 = object : EntityInsertAdapter<TransactionTagEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `transaction_tags` (`transactionId`,`tagId`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionTagEntity) {
        statement.bindLong(1, entity.transactionId)
        statement.bindLong(2, entity.tagId)
      }
    }
    this.__deleteAdapterOfTransactionEntity = object : EntityDeleteOrUpdateAdapter<TransactionEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `transactions` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfAccountEntity = object : EntityDeleteOrUpdateAdapter<AccountEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `accounts` SET `id` = ?,`name` = ?,`currency` = ?,`openingBalanceAtomic` = ?,`openingDate` = ?,`groupLabel` = ?,`trackerId` = ?,`archived` = ?,`type` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: AccountEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        statement.bindLong(4, entity.openingBalanceAtomic)
        statement.bindLong(5, entity.openingDate)
        val _tmpGroupLabel: String? = entity.groupLabel
        if (_tmpGroupLabel == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpGroupLabel)
        }
        val _tmpTrackerId: String? = entity.trackerId
        if (_tmpTrackerId == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpTrackerId)
        }
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindText(9, entity.type)
        statement.bindLong(10, entity.id)
      }
    }
    this.__updateAdapterOfIncomeSourceEntity = object : EntityDeleteOrUpdateAdapter<IncomeSourceEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `income_sources` SET `id` = ?,`name` = ?,`currency` = ?,`archived` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: IncomeSourceEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.currency)
        val _tmp: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        statement.bindLong(5, entity.id)
      }
    }
    this.__updateAdapterOfCategoryEntity = object : EntityDeleteOrUpdateAdapter<CategoryEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `categories` SET `id` = ?,`name` = ?,`parentId` = ?,`pinned` = ?,`archived` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpParentId: Long? = entity.parentId
        if (_tmpParentId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpParentId)
        }
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(4, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
        statement.bindLong(6, entity.id)
      }
    }
    this.__updateAdapterOfTagEntity = object : EntityDeleteOrUpdateAdapter<TagEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `tags` SET `id` = ?,`name` = ?,`pinned` = ?,`archived` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TagEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: Int = if (entity.pinned) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        val _tmp_1: Int = if (entity.archived) 1 else 0
        statement.bindLong(4, _tmp_1.toLong())
        statement.bindLong(5, entity.id)
      }
    }
    this.__updateAdapterOfTransactionEntity = object : EntityDeleteOrUpdateAdapter<TransactionEntity>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `transactions` SET `id` = ?,`type` = ?,`timestamp` = ?,`categoryId` = ?,`incomeSourceId` = ?,`incomeSourceAmountAtomic` = ?,`note` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.type)
        statement.bindLong(3, entity.timestamp)
        val _tmpCategoryId: Long? = entity.categoryId
        if (_tmpCategoryId == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpCategoryId)
        }
        val _tmpIncomeSourceId: Long? = entity.incomeSourceId
        if (_tmpIncomeSourceId == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpIncomeSourceId)
        }
        val _tmpIncomeSourceAmountAtomic: Long? = entity.incomeSourceAmountAtomic
        if (_tmpIncomeSourceAmountAtomic == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmpIncomeSourceAmountAtomic)
        }
        statement.bindText(7, entity.note)
        statement.bindLong(8, entity.createdAt)
        statement.bindLong(9, entity.updatedAt)
        statement.bindLong(10, entity.id)
      }
    }
  }

  public override suspend fun insertAccount(account: AccountEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfAccountEntity.insertAndReturnId(_connection, account)
    _result
  }

  public override suspend fun insertIncomeSource(source: IncomeSourceEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfIncomeSourceEntity.insertAndReturnId(_connection, source)
    _result
  }

  public override suspend fun insertCategory(category: CategoryEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfCategoryEntity.insertAndReturnId(_connection, category)
    _result
  }

  public override suspend fun insertTag(tag: TagEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfTagEntity.insertAndReturnId(_connection, tag)
    _result
  }

  public override suspend fun insertTransaction(transaction: TransactionEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfTransactionEntity.insertAndReturnId(_connection, transaction)
    _result
  }

  public override suspend fun insertLegs(legs: List<TransactionLegEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionLegEntity.insert(_connection, legs)
  }

  public override suspend fun insertTransactionTags(tags: List<TransactionTagEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionTagEntity.insert(_connection, tags)
  }

  public override suspend fun restoreAccounts(items: List<AccountEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAccountEntity_1.insert(_connection, items)
  }

  public override suspend fun restoreIncomeSources(items: List<IncomeSourceEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfIncomeSourceEntity_1.insert(_connection, items)
  }

  public override suspend fun restoreCategories(items: List<CategoryEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCategoryEntity_1.insert(_connection, items)
  }

  public override suspend fun restoreTags(items: List<TagEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTagEntity_1.insert(_connection, items)
  }

  public override suspend fun restoreTransactions(items: List<TransactionEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity_1.insert(_connection, items)
  }

  public override suspend fun restoreLegs(items: List<TransactionLegEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionLegEntity.insert(_connection, items)
  }

  public override suspend fun restoreTransactionTags(items: List<TransactionTagEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionTagEntity_1.insert(_connection, items)
  }

  public override suspend fun deleteTransaction(transaction: TransactionEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfTransactionEntity.handle(_connection, transaction)
  }

  public override suspend fun updateAccount(account: AccountEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfAccountEntity.handle(_connection, account)
  }

  public override suspend fun updateIncomeSource(source: IncomeSourceEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfIncomeSourceEntity.handle(_connection, source)
  }

  public override suspend fun updateCategory(category: CategoryEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfCategoryEntity.handle(_connection, category)
  }

  public override suspend fun updateTag(tag: TagEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfTagEntity.handle(_connection, tag)
  }

  public override suspend fun updateTransaction(transaction: TransactionEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfTransactionEntity.handle(_connection, transaction)
  }

  public override fun observeAccounts(): Flow<List<AccountEntity>> {
    val _sql: String = "SELECT * FROM accounts ORDER BY archived ASC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("accounts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfOpeningBalanceAtomic: Int = getColumnIndexOrThrow(_stmt, "openingBalanceAtomic")
        val _columnIndexOfOpeningDate: Int = getColumnIndexOrThrow(_stmt, "openingDate")
        val _columnIndexOfGroupLabel: Int = getColumnIndexOrThrow(_stmt, "groupLabel")
        val _columnIndexOfTrackerId: Int = getColumnIndexOrThrow(_stmt, "trackerId")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _result: MutableList<AccountEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AccountEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpOpeningBalanceAtomic: Long
          _tmpOpeningBalanceAtomic = _stmt.getLong(_columnIndexOfOpeningBalanceAtomic)
          val _tmpOpeningDate: Long
          _tmpOpeningDate = _stmt.getLong(_columnIndexOfOpeningDate)
          val _tmpGroupLabel: String?
          if (_stmt.isNull(_columnIndexOfGroupLabel)) {
            _tmpGroupLabel = null
          } else {
            _tmpGroupLabel = _stmt.getText(_columnIndexOfGroupLabel)
          }
          val _tmpTrackerId: String?
          if (_stmt.isNull(_columnIndexOfTrackerId)) {
            _tmpTrackerId = null
          } else {
            _tmpTrackerId = _stmt.getText(_columnIndexOfTrackerId)
          }
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          _item = AccountEntity(_tmpId,_tmpName,_tmpCurrency,_tmpOpeningBalanceAtomic,_tmpOpeningDate,_tmpGroupLabel,_tmpTrackerId,_tmpArchived,_tmpType)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActiveAccounts(): Flow<List<AccountEntity>> {
    val _sql: String = "SELECT * FROM accounts WHERE archived = 0 ORDER BY name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("accounts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfOpeningBalanceAtomic: Int = getColumnIndexOrThrow(_stmt, "openingBalanceAtomic")
        val _columnIndexOfOpeningDate: Int = getColumnIndexOrThrow(_stmt, "openingDate")
        val _columnIndexOfGroupLabel: Int = getColumnIndexOrThrow(_stmt, "groupLabel")
        val _columnIndexOfTrackerId: Int = getColumnIndexOrThrow(_stmt, "trackerId")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _result: MutableList<AccountEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AccountEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpOpeningBalanceAtomic: Long
          _tmpOpeningBalanceAtomic = _stmt.getLong(_columnIndexOfOpeningBalanceAtomic)
          val _tmpOpeningDate: Long
          _tmpOpeningDate = _stmt.getLong(_columnIndexOfOpeningDate)
          val _tmpGroupLabel: String?
          if (_stmt.isNull(_columnIndexOfGroupLabel)) {
            _tmpGroupLabel = null
          } else {
            _tmpGroupLabel = _stmt.getText(_columnIndexOfGroupLabel)
          }
          val _tmpTrackerId: String?
          if (_stmt.isNull(_columnIndexOfTrackerId)) {
            _tmpTrackerId = null
          } else {
            _tmpTrackerId = _stmt.getText(_columnIndexOfTrackerId)
          }
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          _item = AccountEntity(_tmpId,_tmpName,_tmpCurrency,_tmpOpeningBalanceAtomic,_tmpOpeningDate,_tmpGroupLabel,_tmpTrackerId,_tmpArchived,_tmpType)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAccount(id: Long): AccountEntity? {
    val _sql: String = "SELECT * FROM accounts WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfOpeningBalanceAtomic: Int = getColumnIndexOrThrow(_stmt, "openingBalanceAtomic")
        val _columnIndexOfOpeningDate: Int = getColumnIndexOrThrow(_stmt, "openingDate")
        val _columnIndexOfGroupLabel: Int = getColumnIndexOrThrow(_stmt, "groupLabel")
        val _columnIndexOfTrackerId: Int = getColumnIndexOrThrow(_stmt, "trackerId")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _result: AccountEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpOpeningBalanceAtomic: Long
          _tmpOpeningBalanceAtomic = _stmt.getLong(_columnIndexOfOpeningBalanceAtomic)
          val _tmpOpeningDate: Long
          _tmpOpeningDate = _stmt.getLong(_columnIndexOfOpeningDate)
          val _tmpGroupLabel: String?
          if (_stmt.isNull(_columnIndexOfGroupLabel)) {
            _tmpGroupLabel = null
          } else {
            _tmpGroupLabel = _stmt.getText(_columnIndexOfGroupLabel)
          }
          val _tmpTrackerId: String?
          if (_stmt.isNull(_columnIndexOfTrackerId)) {
            _tmpTrackerId = null
          } else {
            _tmpTrackerId = _stmt.getText(_columnIndexOfTrackerId)
          }
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          _result = AccountEntity(_tmpId,_tmpName,_tmpCurrency,_tmpOpeningBalanceAtomic,_tmpOpeningDate,_tmpGroupLabel,_tmpTrackerId,_tmpArchived,_tmpType)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeIncomeSources(): Flow<List<IncomeSourceEntity>> {
    val _sql: String = "SELECT * FROM income_sources ORDER BY archived ASC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("income_sources")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<IncomeSourceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: IncomeSourceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          _item = IncomeSourceEntity(_tmpId,_tmpName,_tmpCurrency,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActiveIncomeSources(): Flow<List<IncomeSourceEntity>> {
    val _sql: String = "SELECT * FROM income_sources WHERE archived = 0 ORDER BY name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("income_sources")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<IncomeSourceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: IncomeSourceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          _item = IncomeSourceEntity(_tmpId,_tmpName,_tmpCurrency,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeCategories(): Flow<List<CategoryEntity>> {
    val _sql: String = "SELECT * FROM categories ORDER BY archived ASC, pinned DESC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("categories")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parentId")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<CategoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpParentId: Long?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getLong(_columnIndexOfParentId)
          }
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = CategoryEntity(_tmpId,_tmpName,_tmpParentId,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActiveCategories(): Flow<List<CategoryEntity>> {
    val _sql: String = "SELECT * FROM categories WHERE archived = 0 ORDER BY pinned DESC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("categories")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parentId")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<CategoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpParentId: Long?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getLong(_columnIndexOfParentId)
          }
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = CategoryEntity(_tmpId,_tmpName,_tmpParentId,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTags(): Flow<List<TagEntity>> {
    val _sql: String = "SELECT * FROM tags ORDER BY archived ASC, pinned DESC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("tags")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<TagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TagEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = TagEntity(_tmpId,_tmpName,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeActiveTags(): Flow<List<TagEntity>> {
    val _sql: String = "SELECT * FROM tags WHERE archived = 0 ORDER BY pinned DESC, name COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("tags")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<TagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TagEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = TagEntity(_tmpId,_tmpName,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTransactions(): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions ORDER BY timestamp DESC, id DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfIncomeSourceId: Int = getColumnIndexOrThrow(_stmt, "incomeSourceId")
        val _columnIndexOfIncomeSourceAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "incomeSourceAmountAtomic")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_columnIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId)
          }
          val _tmpIncomeSourceId: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceId)) {
            _tmpIncomeSourceId = null
          } else {
            _tmpIncomeSourceId = _stmt.getLong(_columnIndexOfIncomeSourceId)
          }
          val _tmpIncomeSourceAmountAtomic: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceAmountAtomic)) {
            _tmpIncomeSourceAmountAtomic = null
          } else {
            _tmpIncomeSourceAmountAtomic = _stmt.getLong(_columnIndexOfIncomeSourceAmountAtomic)
          }
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _item = TransactionEntity(_tmpId,_tmpType,_tmpTimestamp,_tmpCategoryId,_tmpIncomeSourceId,_tmpIncomeSourceAmountAtomic,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTransaction(id: Long): TransactionEntity? {
    val _sql: String = "SELECT * FROM transactions WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfIncomeSourceId: Int = getColumnIndexOrThrow(_stmt, "incomeSourceId")
        val _columnIndexOfIncomeSourceAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "incomeSourceAmountAtomic")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: TransactionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_columnIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId)
          }
          val _tmpIncomeSourceId: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceId)) {
            _tmpIncomeSourceId = null
          } else {
            _tmpIncomeSourceId = _stmt.getLong(_columnIndexOfIncomeSourceId)
          }
          val _tmpIncomeSourceAmountAtomic: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceAmountAtomic)) {
            _tmpIncomeSourceAmountAtomic = null
          } else {
            _tmpIncomeSourceAmountAtomic = _stmt.getLong(_columnIndexOfIncomeSourceAmountAtomic)
          }
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _result = TransactionEntity(_tmpId,_tmpType,_tmpTimestamp,_tmpCategoryId,_tmpIncomeSourceId,_tmpIncomeSourceAmountAtomic,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLastTransactionId(type: String): Long? {
    val _sql: String = "SELECT id FROM transactions WHERE type = ? ORDER BY timestamp DESC, id DESC LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, type)
        val _result: Long?
        if (_stmt.step()) {
          if (_stmt.isNull(0)) {
            _result = null
          } else {
            _result = _stmt.getLong(0)
          }
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLegs(transactionId: Long): List<TransactionLegEntity> {
    val _sql: String = "SELECT * FROM transaction_legs WHERE transactionId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transactionId)
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "amountAtomic")
        val _result: MutableList<TransactionLegEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionLegEntity
          val _tmpTransactionId: Long
          _tmpTransactionId = _stmt.getLong(_columnIndexOfTransactionId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpAmountAtomic: Long
          _tmpAmountAtomic = _stmt.getLong(_columnIndexOfAmountAtomic)
          _item = TransactionLegEntity(_tmpTransactionId,_tmpAccountId,_tmpAmountAtomic)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeLegs(): Flow<List<TransactionLegEntity>> {
    val _sql: String = "SELECT * FROM transaction_legs"
    return createFlow(__db, false, arrayOf("transaction_legs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "amountAtomic")
        val _result: MutableList<TransactionLegEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionLegEntity
          val _tmpTransactionId: Long
          _tmpTransactionId = _stmt.getLong(_columnIndexOfTransactionId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpAmountAtomic: Long
          _tmpAmountAtomic = _stmt.getLong(_columnIndexOfAmountAtomic)
          _item = TransactionLegEntity(_tmpTransactionId,_tmpAccountId,_tmpAmountAtomic)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTagIds(transactionId: Long): List<Long> {
    val _sql: String = "SELECT tagId FROM transaction_tags WHERE transactionId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transactionId)
        val _result: MutableList<Long> = mutableListOf()
        while (_stmt.step()) {
          val _item: Long
          _item = _stmt.getLong(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTransactionTags(): Flow<List<TransactionTagEntity>> {
    val _sql: String = "SELECT * FROM transaction_tags"
    return createFlow(__db, false, arrayOf("transaction_tags")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfTagId: Int = getColumnIndexOrThrow(_stmt, "tagId")
        val _result: MutableList<TransactionTagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionTagEntity
          val _tmpTransactionId: Long
          _tmpTransactionId = _stmt.getLong(_columnIndexOfTransactionId)
          val _tmpTagId: Long
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId)
          _item = TransactionTagEntity(_tmpTransactionId,_tmpTagId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun suggestedCategories(type: String, limit: Int): List<CategoryUsage> {
    val _sql: String = """
        |
        |        SELECT c.id, c.name, c.parentId, c.pinned, c.archived,
        |               MAX(t.timestamp) AS lastUsed,
        |               COUNT(t.id) AS useCount
        |        FROM categories c
        |        LEFT JOIN transactions t
        |          ON t.categoryId = c.id AND t.type = ?
        |        WHERE c.archived = 0
        |        GROUP BY c.id
        |        ORDER BY c.pinned DESC,
        |                 CASE WHEN lastUsed IS NULL THEN 1 ELSE 0 END,
        |                 lastUsed DESC,
        |                 useCount DESC,
        |                 c.name COLLATE NOCASE ASC
        |        LIMIT ?
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, type)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = 0
        val _columnIndexOfName: Int = 1
        val _columnIndexOfParentId: Int = 2
        val _columnIndexOfPinned: Int = 3
        val _columnIndexOfArchived: Int = 4
        val _columnIndexOfLastUsed: Int = 5
        val _columnIndexOfUseCount: Int = 6
        val _result: MutableList<CategoryUsage> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryUsage
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpParentId: Long?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getLong(_columnIndexOfParentId)
          }
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          val _tmpLastUsed: Long?
          if (_stmt.isNull(_columnIndexOfLastUsed)) {
            _tmpLastUsed = null
          } else {
            _tmpLastUsed = _stmt.getLong(_columnIndexOfLastUsed)
          }
          val _tmpUseCount: Long
          _tmpUseCount = _stmt.getLong(_columnIndexOfUseCount)
          _item = CategoryUsage(_tmpId,_tmpName,_tmpParentId,_tmpPinned,_tmpArchived,_tmpLastUsed,_tmpUseCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun suggestedTags(categoryId: Long?, limit: Int): List<TagUsage> {
    val _sql: String = """
        |
        |        SELECT tg.id, tg.name, tg.pinned, tg.archived,
        |               MAX(t.timestamp) AS lastUsed,
        |               COUNT(tt.transactionId) AS useCount
        |        FROM tags tg
        |        LEFT JOIN transaction_tags tt ON tt.tagId = tg.id
        |        LEFT JOIN transactions t
        |          ON t.id = tt.transactionId AND (? IS NULL OR t.categoryId = ?)
        |        WHERE tg.archived = 0
        |        GROUP BY tg.id
        |        ORDER BY tg.pinned DESC,
        |                 CASE WHEN lastUsed IS NULL THEN 1 ELSE 0 END,
        |                 lastUsed DESC,
        |                 useCount DESC,
        |                 tg.name COLLATE NOCASE ASC
        |        LIMIT ?
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        if (categoryId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, categoryId)
        }
        _argIndex = 2
        if (categoryId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, categoryId)
        }
        _argIndex = 3
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = 0
        val _columnIndexOfName: Int = 1
        val _columnIndexOfPinned: Int = 2
        val _columnIndexOfArchived: Int = 3
        val _columnIndexOfLastUsed: Int = 4
        val _columnIndexOfUseCount: Int = 5
        val _result: MutableList<TagUsage> = mutableListOf()
        while (_stmt.step()) {
          val _item: TagUsage
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          val _tmpLastUsed: Long?
          if (_stmt.isNull(_columnIndexOfLastUsed)) {
            _tmpLastUsed = null
          } else {
            _tmpLastUsed = _stmt.getLong(_columnIndexOfLastUsed)
          }
          val _tmpUseCount: Long
          _tmpUseCount = _stmt.getLong(_columnIndexOfUseCount)
          _item = TagUsage(_tmpId,_tmpName,_tmpPinned,_tmpArchived,_tmpLastUsed,_tmpUseCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllAccounts(): List<AccountEntity> {
    val _sql: String = "SELECT * FROM accounts ORDER BY id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfOpeningBalanceAtomic: Int = getColumnIndexOrThrow(_stmt, "openingBalanceAtomic")
        val _columnIndexOfOpeningDate: Int = getColumnIndexOrThrow(_stmt, "openingDate")
        val _columnIndexOfGroupLabel: Int = getColumnIndexOrThrow(_stmt, "groupLabel")
        val _columnIndexOfTrackerId: Int = getColumnIndexOrThrow(_stmt, "trackerId")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _result: MutableList<AccountEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AccountEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpOpeningBalanceAtomic: Long
          _tmpOpeningBalanceAtomic = _stmt.getLong(_columnIndexOfOpeningBalanceAtomic)
          val _tmpOpeningDate: Long
          _tmpOpeningDate = _stmt.getLong(_columnIndexOfOpeningDate)
          val _tmpGroupLabel: String?
          if (_stmt.isNull(_columnIndexOfGroupLabel)) {
            _tmpGroupLabel = null
          } else {
            _tmpGroupLabel = _stmt.getText(_columnIndexOfGroupLabel)
          }
          val _tmpTrackerId: String?
          if (_stmt.isNull(_columnIndexOfTrackerId)) {
            _tmpTrackerId = null
          } else {
            _tmpTrackerId = _stmt.getText(_columnIndexOfTrackerId)
          }
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          _item = AccountEntity(_tmpId,_tmpName,_tmpCurrency,_tmpOpeningBalanceAtomic,_tmpOpeningDate,_tmpGroupLabel,_tmpTrackerId,_tmpArchived,_tmpType)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllIncomeSources(): List<IncomeSourceEntity> {
    val _sql: String = "SELECT * FROM income_sources ORDER BY id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfCurrency: Int = getColumnIndexOrThrow(_stmt, "currency")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<IncomeSourceEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: IncomeSourceEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpCurrency: String
          _tmpCurrency = _stmt.getText(_columnIndexOfCurrency)
          val _tmpArchived: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp != 0
          _item = IncomeSourceEntity(_tmpId,_tmpName,_tmpCurrency,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllCategories(): List<CategoryEntity> {
    val _sql: String = "SELECT * FROM categories ORDER BY id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfParentId: Int = getColumnIndexOrThrow(_stmt, "parentId")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<CategoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpParentId: Long?
          if (_stmt.isNull(_columnIndexOfParentId)) {
            _tmpParentId = null
          } else {
            _tmpParentId = _stmt.getLong(_columnIndexOfParentId)
          }
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = CategoryEntity(_tmpId,_tmpName,_tmpParentId,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllTags(): List<TagEntity> {
    val _sql: String = "SELECT * FROM tags ORDER BY id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfPinned: Int = getColumnIndexOrThrow(_stmt, "pinned")
        val _columnIndexOfArchived: Int = getColumnIndexOrThrow(_stmt, "archived")
        val _result: MutableList<TagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TagEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpPinned: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfPinned).toInt()
          _tmpPinned = _tmp != 0
          val _tmpArchived: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfArchived).toInt()
          _tmpArchived = _tmp_1 != 0
          _item = TagEntity(_tmpId,_tmpName,_tmpPinned,_tmpArchived)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllTransactions(): List<TransactionEntity> {
    val _sql: String = "SELECT * FROM transactions ORDER BY id ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _columnIndexOfIncomeSourceId: Int = getColumnIndexOrThrow(_stmt, "incomeSourceId")
        val _columnIndexOfIncomeSourceAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "incomeSourceAmountAtomic")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_columnIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_columnIndexOfCategoryId)
          }
          val _tmpIncomeSourceId: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceId)) {
            _tmpIncomeSourceId = null
          } else {
            _tmpIncomeSourceId = _stmt.getLong(_columnIndexOfIncomeSourceId)
          }
          val _tmpIncomeSourceAmountAtomic: Long?
          if (_stmt.isNull(_columnIndexOfIncomeSourceAmountAtomic)) {
            _tmpIncomeSourceAmountAtomic = null
          } else {
            _tmpIncomeSourceAmountAtomic = _stmt.getLong(_columnIndexOfIncomeSourceAmountAtomic)
          }
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          _item = TransactionEntity(_tmpId,_tmpType,_tmpTimestamp,_tmpCategoryId,_tmpIncomeSourceId,_tmpIncomeSourceAmountAtomic,_tmpNote,_tmpCreatedAt,_tmpUpdatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllLegs(): List<TransactionLegEntity> {
    val _sql: String = "SELECT * FROM transaction_legs ORDER BY transactionId ASC, accountId ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfAccountId: Int = getColumnIndexOrThrow(_stmt, "accountId")
        val _columnIndexOfAmountAtomic: Int = getColumnIndexOrThrow(_stmt, "amountAtomic")
        val _result: MutableList<TransactionLegEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionLegEntity
          val _tmpTransactionId: Long
          _tmpTransactionId = _stmt.getLong(_columnIndexOfTransactionId)
          val _tmpAccountId: Long
          _tmpAccountId = _stmt.getLong(_columnIndexOfAccountId)
          val _tmpAmountAtomic: Long
          _tmpAmountAtomic = _stmt.getLong(_columnIndexOfAmountAtomic)
          _item = TransactionLegEntity(_tmpTransactionId,_tmpAccountId,_tmpAmountAtomic)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllTransactionTags(): List<TransactionTagEntity> {
    val _sql: String = "SELECT * FROM transaction_tags ORDER BY transactionId ASC, tagId ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfTagId: Int = getColumnIndexOrThrow(_stmt, "tagId")
        val _result: MutableList<TransactionTagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionTagEntity
          val _tmpTransactionId: Long
          _tmpTransactionId = _stmt.getLong(_columnIndexOfTransactionId)
          val _tmpTagId: Long
          _tmpTagId = _stmt.getLong(_columnIndexOfTagId)
          _item = TransactionTagEntity(_tmpTransactionId,_tmpTagId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteLegs(transactionId: Long) {
    val _sql: String = "DELETE FROM transaction_legs WHERE transactionId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transactionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTransactionTags(transactionId: Long) {
    val _sql: String = "DELETE FROM transaction_tags WHERE transactionId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, transactionId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearTransactionTags() {
    val _sql: String = "DELETE FROM transaction_tags"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearLegs() {
    val _sql: String = "DELETE FROM transaction_legs"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearTransactions() {
    val _sql: String = "DELETE FROM transactions"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearTags() {
    val _sql: String = "DELETE FROM tags"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearCategories() {
    val _sql: String = "DELETE FROM categories"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearIncomeSources() {
    val _sql: String = "DELETE FROM income_sources"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAccounts() {
    val _sql: String = "DELETE FROM accounts"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
