package com.project1k.money.data

import androidx.room.withTransaction

class MoneyRepository(private val db: MoneyDatabase) {
    private val dao = db.dao()

    val accounts = dao.observeAccounts()
    val activeAccounts = dao.observeActiveAccounts()
    val incomeSources = dao.observeIncomeSources()
    val activeIncomeSources = dao.observeActiveIncomeSources()
    val categories = dao.observeCategories()
    val activeCategories = dao.observeActiveCategories()
    val tags = dao.observeTags()
    val activeTags = dao.observeActiveTags()
    val transactions = dao.observeTransactions()
    val legs = dao.observeLegs()
    val transactionTags = dao.observeTransactionTags()

    suspend fun saveAccount(account: AccountEntity): Long {
        require(account.type in AccountType.all) { "Unknown account type" }
        return if (account.id == 0L) dao.insertAccount(account) else {
            dao.updateAccount(account)
            account.id
        }
    }

    suspend fun saveIncomeSource(source: IncomeSourceEntity): Long =
        if (source.id == 0L) dao.insertIncomeSource(source) else {
            dao.updateIncomeSource(source)
            source.id
        }

    suspend fun saveCategory(category: CategoryEntity): Long =
        if (category.id == 0L) dao.insertCategory(category) else {
            dao.updateCategory(category)
            category.id
        }

    suspend fun saveTag(tag: TagEntity): Long =
        if (tag.id == 0L) dao.insertTag(tag) else {
            dao.updateTag(tag)
            tag.id
        }

    suspend fun deleteTransaction(id: Long) {
        val tx = dao.getTransaction(id) ?: return
        dao.deleteTransaction(tx)
    }

    suspend fun loadTransaction(id: Long): LoadedTransaction? {
        val tx = dao.getTransaction(id) ?: return null
        return LoadedTransaction(tx, dao.getLegs(id), dao.getTagIds(id))
    }

    suspend fun loadLastTransaction(type: String): LoadedTransaction? {
        val id = dao.getLastTransactionId(type) ?: return null
        return loadTransaction(id)
    }

    suspend fun suggestedTags(categoryId: Long?) = dao.suggestedTags(categoryId)

    suspend fun saveExpense(
        existingId: Long?,
        timestamp: Long,
        accountId: Long,
        amountAtomic: Long,
        categoryId: Long,
        tagIds: Set<Long>,
        note: String
    ): Long = db.withTransaction {
        require(amountAtomic > 0)
        val now = System.currentTimeMillis()
        val txId = upsertTransaction(
            existingId = existingId,
            type = TransactionType.EXPENSE,
            timestamp = timestamp,
            categoryId = categoryId,
            incomeSourceId = null,
            incomeSourceAmountAtomic = null,
            note = note,
            now = now
        )
        dao.insertLegs(listOf(TransactionLegEntity(txId, accountId, -amountAtomic)))
        dao.insertTransactionTags(tagIds.map { TransactionTagEntity(txId, it) })
        txId
    }

    suspend fun saveIncome(
        existingId: Long?,
        timestamp: Long,
        incomeSourceId: Long,
        sourceAmountAtomic: Long,
        toAccountId: Long,
        receivedAmountAtomic: Long,
        tagIds: Set<Long>,
        note: String
    ): Long = db.withTransaction {
        require(sourceAmountAtomic > 0 && receivedAmountAtomic > 0)
        val now = System.currentTimeMillis()
        val txId = upsertTransaction(
            existingId = existingId,
            type = TransactionType.INCOME,
            timestamp = timestamp,
            categoryId = null,
            incomeSourceId = incomeSourceId,
            incomeSourceAmountAtomic = sourceAmountAtomic,
            note = note,
            now = now
        )
        dao.insertLegs(listOf(TransactionLegEntity(txId, toAccountId, receivedAmountAtomic)))
        dao.insertTransactionTags(tagIds.map { TransactionTagEntity(txId, it) })
        txId
    }

    suspend fun saveTransfer(
        existingId: Long?,
        timestamp: Long,
        fromAccountId: Long,
        toAccountId: Long,
        amountFromAtomic: Long,
        amountToAtomic: Long,
        tagIds: Set<Long>,
        note: String
    ): Long = db.withTransaction {
        require(fromAccountId != toAccountId)
        require(amountFromAtomic > 0 && amountToAtomic > 0)
        val now = System.currentTimeMillis()
        val txId = upsertTransaction(
            existingId = existingId,
            type = TransactionType.TRANSFER,
            timestamp = timestamp,
            categoryId = null,
            incomeSourceId = null,
            incomeSourceAmountAtomic = null,
            note = note,
            now = now
        )
        dao.insertLegs(
            listOf(
                TransactionLegEntity(txId, fromAccountId, -amountFromAtomic),
                TransactionLegEntity(txId, toAccountId, amountToAtomic)
            )
        )
        dao.insertTransactionTags(tagIds.map { TransactionTagEntity(txId, it) })
        txId
    }

    private suspend fun upsertTransaction(
        existingId: Long?,
        type: String,
        timestamp: Long,
        categoryId: Long?,
        incomeSourceId: Long?,
        incomeSourceAmountAtomic: Long?,
        note: String,
        now: Long
    ): Long {
        if (existingId == null) {
            return dao.insertTransaction(
                TransactionEntity(
                    type = type,
                    timestamp = timestamp,
                    categoryId = categoryId,
                    incomeSourceId = incomeSourceId,
                    incomeSourceAmountAtomic = incomeSourceAmountAtomic,
                    note = note,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }

        val old = dao.getTransaction(existingId) ?: error("Transaction not found")
        dao.updateTransaction(
            old.copy(
                type = type,
                timestamp = timestamp,
                categoryId = categoryId,
                incomeSourceId = incomeSourceId,
                incomeSourceAmountAtomic = incomeSourceAmountAtomic,
                note = note,
                updatedAt = now
            )
        )
        dao.deleteLegs(existingId)
        dao.deleteTransactionTags(existingId)
        return existingId
    }
}

data class LoadedTransaction(
    val transaction: TransactionEntity,
    val legs: List<TransactionLegEntity>,
    val tagIds: List<Long>
)
