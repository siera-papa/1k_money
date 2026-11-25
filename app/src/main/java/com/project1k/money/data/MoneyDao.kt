package com.project1k.money.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneyDao {
    @Query("SELECT * FROM accounts ORDER BY archived ASC, name COLLATE NOCASE ASC")
    fun observeAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE archived = 0 ORDER BY name COLLATE NOCASE ASC")
    fun observeActiveAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id LIMIT 1")
    suspend fun getAccount(id: Long): AccountEntity?

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("SELECT * FROM income_sources ORDER BY archived ASC, name COLLATE NOCASE ASC")
    fun observeIncomeSources(): Flow<List<IncomeSourceEntity>>

    @Query("SELECT * FROM income_sources WHERE archived = 0 ORDER BY name COLLATE NOCASE ASC")
    fun observeActiveIncomeSources(): Flow<List<IncomeSourceEntity>>

    @Insert
    suspend fun insertIncomeSource(source: IncomeSourceEntity): Long

    @Update
    suspend fun updateIncomeSource(source: IncomeSourceEntity)

    @Query("SELECT * FROM categories ORDER BY archived ASC, pinned DESC, name COLLATE NOCASE ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE archived = 0 ORDER BY pinned DESC, name COLLATE NOCASE ASC")
    fun observeActiveCategories(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("SELECT * FROM tags ORDER BY archived ASC, pinned DESC, name COLLATE NOCASE ASC")
    fun observeTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE archived = 0 ORDER BY pinned DESC, name COLLATE NOCASE ASC")
    fun observeActiveTags(): Flow<List<TagEntity>>

    @Insert
    suspend fun insertTag(tag: TagEntity): Long

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC, id DESC")
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransaction(id: Long): TransactionEntity?

    @Query("SELECT id FROM transactions WHERE type = :type ORDER BY timestamp DESC, id DESC LIMIT 1")
    suspend fun getLastTransactionId(type: String): Long?

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transaction_legs WHERE transactionId = :transactionId")
    suspend fun deleteLegs(transactionId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLegs(legs: List<TransactionLegEntity>)

    @Query("SELECT * FROM transaction_legs WHERE transactionId = :transactionId")
    suspend fun getLegs(transactionId: Long): List<TransactionLegEntity>

    @Query("SELECT * FROM transaction_legs")
    fun observeLegs(): Flow<List<TransactionLegEntity>>

    @Query("DELETE FROM transaction_tags WHERE transactionId = :transactionId")
    suspend fun deleteTransactionTags(transactionId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransactionTags(tags: List<TransactionTagEntity>)

    @Query("SELECT tagId FROM transaction_tags WHERE transactionId = :transactionId")
    suspend fun getTagIds(transactionId: Long): List<Long>

    @Query("SELECT * FROM transaction_tags")
    fun observeTransactionTags(): Flow<List<TransactionTagEntity>>

    @Query(
        """
        SELECT c.id, c.name, c.parentId, c.pinned, c.archived,
               MAX(t.timestamp) AS lastUsed,
               COUNT(t.id) AS useCount
        FROM categories c
        LEFT JOIN transactions t
          ON t.categoryId = c.id AND t.type = :type
        WHERE c.archived = 0
        GROUP BY c.id
        ORDER BY c.pinned DESC,
                 CASE WHEN lastUsed IS NULL THEN 1 ELSE 0 END,
                 lastUsed DESC,
                 useCount DESC,
                 c.name COLLATE NOCASE ASC
        LIMIT :limit
        """
    )
    suspend fun suggestedCategories(type: String, limit: Int = 6): List<CategoryUsage>

    @Query(
        """
        SELECT tg.id, tg.name, tg.pinned, tg.archived,
               MAX(t.timestamp) AS lastUsed,
               COUNT(tt.transactionId) AS useCount
        FROM tags tg
        LEFT JOIN transaction_tags tt ON tt.tagId = tg.id
        LEFT JOIN transactions t
          ON t.id = tt.transactionId AND (:categoryId IS NULL OR t.categoryId = :categoryId)
        WHERE tg.archived = 0
        GROUP BY tg.id
        ORDER BY tg.pinned DESC,
                 CASE WHEN lastUsed IS NULL THEN 1 ELSE 0 END,
                 lastUsed DESC,
                 useCount DESC,
                 tg.name COLLATE NOCASE ASC
        LIMIT :limit
        """
    )
    suspend fun suggestedTags(categoryId: Long?, limit: Int = 8): List<TagUsage>

    @Query("SELECT * FROM accounts ORDER BY id ASC")
    suspend fun getAllAccounts(): List<AccountEntity>

    @Query("SELECT * FROM income_sources ORDER BY id ASC")
    suspend fun getAllIncomeSources(): List<IncomeSourceEntity>

    @Query("SELECT * FROM categories ORDER BY id ASC")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("SELECT * FROM tags ORDER BY id ASC")
    suspend fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM transactions ORDER BY id ASC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transaction_legs ORDER BY transactionId ASC, accountId ASC")
    suspend fun getAllLegs(): List<TransactionLegEntity>

    @Query("SELECT * FROM transaction_tags ORDER BY transactionId ASC, tagId ASC")
    suspend fun getAllTransactionTags(): List<TransactionTagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreAccounts(items: List<AccountEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreIncomeSources(items: List<IncomeSourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreCategories(items: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreTags(items: List<TagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreTransactions(items: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreLegs(items: List<TransactionLegEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun restoreTransactionTags(items: List<TransactionTagEntity>)

    @Query("DELETE FROM transaction_tags")
    suspend fun clearTransactionTags()

    @Query("DELETE FROM transaction_legs")
    suspend fun clearLegs()

    @Query("DELETE FROM transactions")
    suspend fun clearTransactions()

    @Query("DELETE FROM tags")
    suspend fun clearTags()

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Query("DELETE FROM income_sources")
    suspend fun clearIncomeSources()

    @Query("DELETE FROM accounts")
    suspend fun clearAccounts()
}
