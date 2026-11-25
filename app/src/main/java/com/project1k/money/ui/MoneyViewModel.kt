package com.project1k.money.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project1k.money.MoneyApplication
import com.project1k.money.data.AccountEntity
import com.project1k.money.data.BackupManager
import com.project1k.money.data.BackupScheduler
import com.project1k.money.data.BackupWriteResult
import com.project1k.money.data.RestoreResult
import com.project1k.money.data.CategoryEntity
import com.project1k.money.data.IncomeSourceEntity
import com.project1k.money.data.MoneyCodec
import com.project1k.money.data.MoneyRepository
import com.project1k.money.data.TagEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoneyViewModel(app: Application) : AndroidViewModel(app) {
    private val moneyApplication = app as MoneyApplication
    val repository: MoneyRepository = moneyApplication.repository
    private val backupManager = BackupManager(app, moneyApplication.database)

    private val _backupUi = MutableStateFlow(
        BackupUiState(
            folderConfigured = backupManager.backupFolderUri() != null,
            folderLabel = backupManager.backupFolderLabel()
        )
    )
    val backupUi: StateFlow<BackupUiState> = _backupUi.asStateFlow()

    val accounts = repository.accounts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val activeAccounts = repository.activeAccounts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val incomeSources = repository.incomeSources.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val activeIncomeSources = repository.activeIncomeSources.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val categories = repository.categories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val activeCategories = repository.activeCategories.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val tags = repository.tags.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val activeTags = repository.activeTags.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val transactions = repository.transactions.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val legs = repository.legs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val transactionTags = repository.transactionTags.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private data class RowInputs(
        val txs: List<com.project1k.money.data.TransactionEntity>,
        val legs: List<com.project1k.money.data.TransactionLegEntity>,
        val accounts: List<AccountEntity>,
        val categories: List<CategoryEntity>,
        val txTags: List<com.project1k.money.data.TransactionTagEntity>
    )

    private val rowInputs = combine(
        repository.transactions,
        repository.legs,
        repository.accounts,
        repository.categories,
        repository.transactionTags
    ) { txs, legs, accounts, categories, txTags ->
        RowInputs(txs, legs, accounts, categories, txTags)
    }

    private data class LookupInputs(
        val tags: List<TagEntity>,
        val incomeSources: List<IncomeSourceEntity>
    )

    private val lookupInputs = combine(repository.tags, repository.incomeSources) { tags, incomeSources ->
        LookupInputs(tags, incomeSources)
    }

    val transactionRows = combine(rowInputs, lookupInputs) { input, lookups ->
        val accountMap = input.accounts.associateBy { it.id }
        val categoryMap = input.categories.associateBy { it.id }
        val tagMap = lookups.tags.associateBy { it.id }
        val sourceMap = lookups.incomeSources.associateBy { it.id }
        val tagsByTx = input.txTags.groupBy { it.transactionId }
        val legsByTx = input.legs.groupBy { it.transactionId }

        input.txs.map { tx ->
            TransactionRow(
                id = tx.id,
                type = tx.type,
                timestamp = tx.timestamp,
                categoryName = tx.categoryId?.let { id ->
                    categoryMap[id]?.let { category ->
                        category.parentId?.let { parentId ->
                            categoryMap[parentId]?.let { parent -> "${parent.name} › ${category.name}" }
                        } ?: category.name
                    }
                },
                incomeSourceName = tx.incomeSourceId?.let { sourceMap[it]?.name },
                incomeSourceCurrency = tx.incomeSourceId?.let { sourceMap[it]?.currency },
                incomeSourceAmountAtomic = tx.incomeSourceAmountAtomic,
                note = tx.note,
                legs = legsByTx[tx.id].orEmpty().mapNotNull { leg ->
                    accountMap[leg.accountId]?.let { account ->
                        TransactionLegRow(
                            accountId = account.id,
                            accountName = account.name,
                            currency = account.currency,
                            amountAtomic = leg.amountAtomic
                        )
                    }
                },
                tags = tagsByTx[tx.id].orEmpty().mapNotNull { tagMap[it.tagId]?.name }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveAccount(
        existing: AccountEntity?,
        name: String,
        type: String,
        currency: String,
        openingBalanceText: String,
        trackerIdText: String,
        archived: Boolean
    ) {
        val atomic = MoneyCodec.parseAtomic(openingBalanceText.ifBlank { "0" }, currency) ?: return
        viewModelScope.launch {
            repository.saveAccount(
                (existing ?: AccountEntity(
                    name = name.trim(),
                    currency = currency.uppercase(),
                    openingBalanceAtomic = atomic,
                    openingDate = System.currentTimeMillis()
                )).copy(
                    name = name.trim(),
                    currency = currency.uppercase(),
                    openingBalanceAtomic = atomic,
                    trackerId = trackerIdText.trim().takeIf { it.isNotEmpty() },
                    type = type,
                    archived = archived
                )
            )
        }
    }

    fun saveIncomeSource(existing: IncomeSourceEntity?, name: String, currency: String, archived: Boolean) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.saveIncomeSource(
                (existing ?: IncomeSourceEntity(name = name.trim(), currency = currency.uppercase())).copy(
                    name = name.trim(),
                    currency = currency.uppercase(),
                    archived = archived
                )
            )
        }
    }

    fun saveCategory(existing: CategoryEntity?, name: String, parentId: Long?, pinned: Boolean, archived: Boolean) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.saveCategory(
                (existing ?: CategoryEntity(name = name.trim())).copy(
                    name = name.trim(),
                    parentId = parentId,
                    pinned = pinned,
                    archived = archived
                )
            )
        }
    }

    fun saveTag(existing: TagEntity?, name: String, pinned: Boolean, archived: Boolean) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.saveTag(
                (existing ?: TagEntity(name = name.trim())).copy(
                    name = name.trim(),
                    pinned = pinned,
                    archived = archived
                )
            )
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch { repository.deleteTransaction(id) }
    }

    fun setBackupFolder(uri: Uri) {
        viewModelScope.launch {
            _backupUi.value = _backupUi.value.copy(busy = true, status = null)
            try {
                backupManager.setBackupFolder(uri)
                BackupScheduler.ensureScheduled(getApplication<Application>())
                val result = backupManager.createAutomaticBackup()
                _backupUi.value = BackupUiState(
                    folderConfigured = true,
                    folderLabel = backupManager.backupFolderLabel(),
                    busy = false,
                    status = when (result) {
                        is BackupWriteResult.Success -> "Folder saved. First backup created: ${result.fileName}"
                        is BackupWriteResult.Error -> "Folder saved, but first backup failed: ${result.message}"
                    }
                )
            } catch (e: Exception) {
                _backupUi.value = _backupUi.value.copy(
                    busy = false,
                    status = "Could not use folder: ${e.message ?: e.javaClass.simpleName}"
                )
            }
        }
    }

    fun backupNow() {
        viewModelScope.launch {
            _backupUi.value = _backupUi.value.copy(busy = true, status = null)
            when (val result = backupManager.createManualBackup()) {
                is BackupWriteResult.Success -> _backupUi.value = _backupUi.value.copy(
                    busy = false,
                    status = "Backup created: ${result.fileName}"
                )
                is BackupWriteResult.Error -> _backupUi.value = _backupUi.value.copy(
                    busy = false,
                    status = "Backup failed: ${result.message}"
                )
            }
        }
    }

    fun restoreBackup(uri: Uri) {
        viewModelScope.launch {
            _backupUi.value = _backupUi.value.copy(busy = true, status = null)
            when (val result = backupManager.restore(uri)) {
                is RestoreResult.Success -> {
                    val whenText = formatAppDateTime(result.summary.createdAt)
                    val suffix = result.warning?.let { " $it" }.orEmpty()
                    _backupUi.value = _backupUi.value.copy(
                        busy = false,
                        status = "Restored backup from $whenText: ${result.summary.accounts} accounts, ${result.summary.transactions} transactions, ${result.summary.categories} categories, ${result.summary.tags} tags.$suffix"
                    )
                }
                is RestoreResult.Error -> _backupUi.value = _backupUi.value.copy(
                    busy = false,
                    status = "Restore failed: ${result.message}"
                )
            }
        }
    }
}

data class BackupUiState(
    val folderConfigured: Boolean = false,
    val folderLabel: String? = null,
    val busy: Boolean = false,
    val status: String? = null
)

data class TransactionLegRow(
    val accountId: Long,
    val accountName: String,
    val currency: String,
    val amountAtomic: Long
)

data class TransactionRow(
    val id: Long,
    val type: String,
    val timestamp: Long,
    val categoryName: String?,
    val incomeSourceName: String?,
    val incomeSourceCurrency: String?,
    val incomeSourceAmountAtomic: Long?,
    val note: String,
    val legs: List<TransactionLegRow>,
    val tags: List<String>
)
