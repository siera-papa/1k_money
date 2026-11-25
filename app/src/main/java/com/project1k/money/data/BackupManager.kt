package com.project1k.money.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.room.withTransaction
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupManager(
    private val context: Context,
    private val database: MoneyDatabase
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val resolver get() = context.contentResolver

    fun backupFolderUri(): Uri? = prefs.getString(KEY_BACKUP_TREE_URI, null)?.let(Uri::parse)

    fun backupFolderLabel(): String? = prefs.getString(KEY_BACKUP_TREE_LABEL, null)

    fun setBackupFolder(uri: Uri) {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        resolver.takePersistableUriPermission(uri, flags)
        prefs.edit()
            .putString(KEY_BACKUP_TREE_URI, uri.toString())
            .putString(KEY_BACKUP_TREE_LABEL, resolveDisplayName(uri) ?: "Selected folder")
            .apply()
    }

    suspend fun createManualBackup(): BackupWriteResult = withContext(Dispatchers.IO) {
        createBackupInternal("manual")
    }

    suspend fun createAutomaticBackup(): BackupWriteResult = withContext(Dispatchers.IO) {
        val result = createBackupInternal("auto")
        if (result is BackupWriteResult.Success) pruneAutomaticBackups(14)
        result
    }

    suspend fun restore(uri: Uri): RestoreResult = withContext(Dispatchers.IO) {
        restoreInternal(uri)
    }

    private suspend fun restoreInternal(uri: Uri): RestoreResult {
        return try {
            val text = resolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                ?: return RestoreResult.Error("Could not open backup file")
            val payload = parsePayload(text)
            val validationError = validate(payload)
            if (validationError != null) return RestoreResult.Error(validationError)

            if (backupFolderUri() != null) {
                when (val safety = createBackupInternal("pre_restore")) {
                    is BackupWriteResult.Error -> return RestoreResult.Error(
                        "Safety backup failed; restore was not started: ${safety.message}"
                    )
                    is BackupWriteResult.Success -> Unit
                }
            }

            database.withTransaction {
                val dao = database.dao()
                dao.clearTransactionTags()
                dao.clearLegs()
                dao.clearTransactions()
                dao.clearTags()
                dao.clearCategories()
                dao.clearIncomeSources()
                dao.clearAccounts()

                if (payload.accounts.isNotEmpty()) dao.restoreAccounts(payload.accounts)
                if (payload.incomeSources.isNotEmpty()) dao.restoreIncomeSources(payload.incomeSources)
                if (payload.categories.isNotEmpty()) dao.restoreCategories(payload.categories)
                if (payload.tags.isNotEmpty()) dao.restoreTags(payload.tags)
                if (payload.transactions.isNotEmpty()) dao.restoreTransactions(payload.transactions)
                if (payload.legs.isNotEmpty()) dao.restoreLegs(payload.legs)
                if (payload.transactionTags.isNotEmpty()) dao.restoreTransactionTags(payload.transactionTags)
            }

            RestoreResult.Success(
                summary = BackupSummary(
                    createdAt = payload.createdAt,
                    accounts = payload.accounts.size,
                    transactions = payload.transactions.size,
                    categories = payload.categories.size,
                    tags = payload.tags.size
                ),
                warning = null
            )
        } catch (e: Exception) {
            RestoreResult.Error(e.message ?: e.javaClass.simpleName)
        }
    }

    private suspend fun createBackupInternal(kind: String): BackupWriteResult {
        val tree = backupFolderUri() ?: return BackupWriteResult.Error("Choose a backup folder first")
        return try {
            val json = buildBackupJson(kind)
            val stamp = FILE_STAMP.format(Date())
            val name = "1K_Money_${kind}_${stamp}.json"
            val parentId = DocumentsContract.getTreeDocumentId(tree)
            val parent = DocumentsContract.buildDocumentUriUsingTree(tree, parentId)
            val fileUri = DocumentsContract.createDocument(resolver, parent, "application/json", name)
                ?: return BackupWriteResult.Error("Could not create backup file")
            resolver.openOutputStream(fileUri, "w")?.bufferedWriter(Charsets.UTF_8)?.use { writer ->
                writer.write(json.toString())
                writer.flush()
            } ?: return BackupWriteResult.Error("Could not write backup file")
            BackupWriteResult.Success(name, fileUri)
        } catch (e: SecurityException) {
            BackupWriteResult.Error("Backup folder permission is no longer available")
        } catch (e: Exception) {
            BackupWriteResult.Error(e.message ?: e.javaClass.simpleName)
        }
    }

    private suspend fun buildBackupJson(kind: String): JSONObject {
        val payload = database.withTransaction {
            val dao = database.dao()
            BackupPayload(
                createdAt = System.currentTimeMillis(),
                accounts = dao.getAllAccounts(),
                incomeSources = dao.getAllIncomeSources(),
                categories = dao.getAllCategories(),
                tags = dao.getAllTags(),
                transactions = dao.getAllTransactions(),
                legs = dao.getAllLegs(),
                transactionTags = dao.getAllTransactionTags()
            )
        }

        return JSONObject().apply {
            put("format", BACKUP_FORMAT)
            put("formatVersion", FORMAT_VERSION)
            put("createdAt", payload.createdAt)
            put("kind", kind)
            put("appVersion", appVersionName())
            put("accounts", JSONArray().apply {
                payload.accounts.forEach { a ->
                    put(JSONObject().apply {
                        put("id", a.id)
                        put("name", a.name)
                        put("type", a.type)
                        put("currency", a.currency)
                        put("openingBalanceAtomic", a.openingBalanceAtomic)
                        put("openingDate", a.openingDate)
                        putNullable("groupLabel", a.groupLabel)
                        putNullable("trackerId", a.trackerId)
                        put("archived", a.archived)
                    })
                }
            })
            put("incomeSources", JSONArray().apply {
                payload.incomeSources.forEach { s ->
                    put(JSONObject().apply {
                        put("id", s.id)
                        put("name", s.name)
                        put("currency", s.currency)
                        put("archived", s.archived)
                    })
                }
            })
            put("categories", JSONArray().apply {
                payload.categories.forEach { c ->
                    put(JSONObject().apply {
                        put("id", c.id)
                        put("name", c.name)
                        putNullable("parentId", c.parentId)
                        put("pinned", c.pinned)
                        put("archived", c.archived)
                    })
                }
            })
            put("tags", JSONArray().apply {
                payload.tags.forEach { t ->
                    put(JSONObject().apply {
                        put("id", t.id)
                        put("name", t.name)
                        put("pinned", t.pinned)
                        put("archived", t.archived)
                    })
                }
            })
            put("transactions", JSONArray().apply {
                payload.transactions.forEach { t ->
                    put(JSONObject().apply {
                        put("id", t.id)
                        put("type", t.type)
                        put("timestamp", t.timestamp)
                        putNullable("categoryId", t.categoryId)
                        putNullable("incomeSourceId", t.incomeSourceId)
                        putNullable("incomeSourceAmountAtomic", t.incomeSourceAmountAtomic)
                        put("note", t.note)
                        put("createdAt", t.createdAt)
                        put("updatedAt", t.updatedAt)
                    })
                }
            })
            put("transactionLegs", JSONArray().apply {
                payload.legs.forEach { l ->
                    put(JSONObject().apply {
                        put("transactionId", l.transactionId)
                        put("accountId", l.accountId)
                        put("amountAtomic", l.amountAtomic)
                    })
                }
            })
            put("transactionTags", JSONArray().apply {
                payload.transactionTags.forEach { tt ->
                    put(JSONObject().apply {
                        put("transactionId", tt.transactionId)
                        put("tagId", tt.tagId)
                    })
                }
            })
        }
    }

    private fun parsePayload(text: String): BackupPayload {
        val root = JSONObject(text)
        if (root.optString("format") != BACKUP_FORMAT) throw IllegalArgumentException("Not a 1K Money backup")
        if (root.optInt("formatVersion", -1) != FORMAT_VERSION) {
            throw IllegalArgumentException("Unsupported backup format version")
        }
        return BackupPayload(
            createdAt = root.getLong("createdAt"),
            accounts = root.getJSONArray("accounts").mapObjects { o ->
                AccountEntity(
                    id = o.getLong("id"),
                    name = o.getString("name"),
                    type = if (o.has("type")) o.getString("type") else AccountType.PRIMARY,
                    currency = o.getString("currency"),
                    openingBalanceAtomic = o.getLong("openingBalanceAtomic"),
                    openingDate = o.getLong("openingDate"),
                    groupLabel = o.optNullableString("groupLabel"),
                    trackerId = o.optNullableString("trackerId"),
                    archived = o.getBoolean("archived")
                )
            },
            incomeSources = root.getJSONArray("incomeSources").mapObjects { o ->
                IncomeSourceEntity(
                    id = o.getLong("id"),
                    name = o.getString("name"),
                    currency = o.getString("currency"),
                    archived = o.getBoolean("archived")
                )
            },
            categories = root.getJSONArray("categories").mapObjects { o ->
                CategoryEntity(
                    id = o.getLong("id"),
                    name = o.getString("name"),
                    parentId = o.optNullableLong("parentId"),
                    pinned = o.getBoolean("pinned"),
                    archived = o.getBoolean("archived")
                )
            },
            tags = root.getJSONArray("tags").mapObjects { o ->
                TagEntity(
                    id = o.getLong("id"),
                    name = o.getString("name"),
                    pinned = o.getBoolean("pinned"),
                    archived = o.getBoolean("archived")
                )
            },
            transactions = root.getJSONArray("transactions").mapObjects { o ->
                TransactionEntity(
                    id = o.getLong("id"),
                    type = o.getString("type"),
                    timestamp = o.getLong("timestamp"),
                    categoryId = o.optNullableLong("categoryId"),
                    incomeSourceId = o.optNullableLong("incomeSourceId"),
                    incomeSourceAmountAtomic = o.optNullableLong("incomeSourceAmountAtomic"),
                    note = o.optString("note", ""),
                    createdAt = o.getLong("createdAt"),
                    updatedAt = o.getLong("updatedAt")
                )
            },
            legs = root.getJSONArray("transactionLegs").mapObjects { o ->
                TransactionLegEntity(
                    transactionId = o.getLong("transactionId"),
                    accountId = o.getLong("accountId"),
                    amountAtomic = o.getLong("amountAtomic")
                )
            },
            transactionTags = root.getJSONArray("transactionTags").mapObjects { o ->
                TransactionTagEntity(
                    transactionId = o.getLong("transactionId"),
                    tagId = o.getLong("tagId")
                )
            }
        )
    }

    private fun validate(p: BackupPayload): String? {
        fun <T> duplicateIds(items: List<T>, id: (T) -> Long): Boolean = items.map(id).toSet().size != items.size
        if (duplicateIds(p.accounts) { it.id }) return "Backup contains duplicate account IDs"
        if (p.accounts.any { it.type !in AccountType.all }) return "Backup has an unknown account type"
        if (duplicateIds(p.incomeSources) { it.id }) return "Backup contains duplicate income source IDs"
        if (duplicateIds(p.categories) { it.id }) return "Backup contains duplicate category IDs"
        if (duplicateIds(p.tags) { it.id }) return "Backup contains duplicate tag IDs"
        if (duplicateIds(p.transactions) { it.id }) return "Backup contains duplicate transaction IDs"

        val accountIds = p.accounts.map { it.id }.toSet()
        val sourceIds = p.incomeSources.map { it.id }.toSet()
        val categoryIds = p.categories.map { it.id }.toSet()
        val tagIds = p.tags.map { it.id }.toSet()
        val txIds = p.transactions.map { it.id }.toSet()

        if (p.categories.any { it.parentId != null && it.parentId !in categoryIds }) return "Backup has a missing parent category"
        if (p.transactions.any { it.categoryId != null && it.categoryId !in categoryIds }) return "Backup has a missing transaction category"
        if (p.transactions.any { it.incomeSourceId != null && it.incomeSourceId !in sourceIds }) return "Backup has a missing income source"
        if (p.transactions.any { it.type !in setOf(TransactionType.EXPENSE, TransactionType.INCOME, TransactionType.TRANSFER) }) return "Backup has an unknown transaction type"
        if (p.legs.any { it.transactionId !in txIds || it.accountId !in accountIds }) return "Backup has an invalid transaction leg"
        if (p.transactionTags.any { it.transactionId !in txIds || it.tagId !in tagIds }) return "Backup has an invalid transaction tag"
        return null
    }

    private fun pruneAutomaticBackups(keep: Int) {
        val tree = backupFolderUri() ?: return
        try {
            val rootId = DocumentsContract.getTreeDocumentId(tree)
            val children = DocumentsContract.buildChildDocumentsUriUsingTree(tree, rootId)
            val found = mutableListOf<Pair<String, Uri>>()
            resolver.query(
                children,
                arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID, DocumentsContract.Document.COLUMN_DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
                val nameIndex = cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    val name = cursor.getString(nameIndex)
                    if (name.startsWith("1K_Money_auto_") && name.endsWith(".json")) {
                        val id = cursor.getString(idIndex)
                        found += name to DocumentsContract.buildDocumentUriUsingTree(tree, id)
                    }
                }
            }
            found.sortedByDescending { it.first }.drop(keep).forEach { (_, uri) ->
                DocumentsContract.deleteDocument(resolver, uri)
            }
        } catch (_: Exception) {
            // Backup creation must not fail just because old-file cleanup could not run.
        }
    }

    private fun resolveDisplayName(uri: Uri): String? {
        return try {
            resolver.query(uri, arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun appVersionName(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "unknown"
        } catch (_: Exception) {
            "unknown"
        }
    }

    private fun JSONObject.putNullable(key: String, value: Any?) {
        put(key, value ?: JSONObject.NULL)
    }

    private fun JSONObject.optNullableLong(key: String): Long? =
        if (!has(key) || isNull(key)) null else getLong(key)

    private fun JSONObject.optNullableString(key: String): String? =
        if (!has(key) || isNull(key)) null else getString(key)

    private inline fun <T> JSONArray.mapObjects(block: (JSONObject) -> T): List<T> =
        List(length()) { index -> block(getJSONObject(index)) }

    companion object {
        private const val PREFS_NAME = "backup_settings"
        private const val KEY_BACKUP_TREE_URI = "backup_tree_uri"
        private const val KEY_BACKUP_TREE_LABEL = "backup_tree_label"
        private const val BACKUP_FORMAT = "project1k-money"
        private const val FORMAT_VERSION = 1
        private val FILE_STAMP = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)
    }
}

data class BackupPayload(
    val createdAt: Long,
    val accounts: List<AccountEntity>,
    val incomeSources: List<IncomeSourceEntity>,
    val categories: List<CategoryEntity>,
    val tags: List<TagEntity>,
    val transactions: List<TransactionEntity>,
    val legs: List<TransactionLegEntity>,
    val transactionTags: List<TransactionTagEntity>
)

data class BackupSummary(
    val createdAt: Long,
    val accounts: Int,
    val transactions: Int,
    val categories: Int,
    val tags: Int
)

sealed interface BackupWriteResult {
    data class Success(val fileName: String, val uri: Uri) : BackupWriteResult
    data class Error(val message: String) : BackupWriteResult
}

sealed interface RestoreResult {
    data class Success(val summary: BackupSummary, val warning: String? = null) : RestoreResult
    data class Error(val message: String) : RestoreResult
}
