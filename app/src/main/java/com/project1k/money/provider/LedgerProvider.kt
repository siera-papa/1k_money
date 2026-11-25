package com.project1k.money.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.project1k.money.data.MoneyDatabase

class LedgerProvider : ContentProvider() {
    private lateinit var db: MoneyDatabase

    override fun onCreate(): Boolean {
        db = MoneyDatabase.get(requireNotNull(context))
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val table = when (MATCHER.match(uri)) {
            ACCOUNTS -> "accounts"
            INCOME_SOURCES -> "income_sources"
            CATEGORIES -> "categories"
            TAGS -> "tags"
            TRANSACTIONS -> "transactions"
            LEGS -> "transaction_legs"
            TRANSACTION_TAGS -> "transaction_tags"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return db.openHelper.readableDatabase.query("SELECT * FROM $table")
    }

    override fun getType(uri: Uri): String = when (MATCHER.match(uri)) {
        ACCOUNTS -> "vnd.android.cursor.dir/vnd.project1k.account"
        INCOME_SOURCES -> "vnd.android.cursor.dir/vnd.project1k.income_source"
        CATEGORIES -> "vnd.android.cursor.dir/vnd.project1k.category"
        TAGS -> "vnd.android.cursor.dir/vnd.project1k.tag"
        TRANSACTIONS -> "vnd.android.cursor.dir/vnd.project1k.transaction"
        LEGS -> "vnd.android.cursor.dir/vnd.project1k.transaction_leg"
        TRANSACTION_TAGS -> "vnd.android.cursor.dir/vnd.project1k.transaction_tag"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri = readOnly()
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = readOnly()
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = readOnly()

    private fun <T> readOnly(): T = throw UnsupportedOperationException("1K Money ledger provider is read-only")

    companion object {
        const val AUTHORITY = "com.project1k.money.ledger"
        val ACCOUNTS_URI: Uri = Uri.parse("content://$AUTHORITY/accounts")
        val INCOME_SOURCES_URI: Uri = Uri.parse("content://$AUTHORITY/income_sources")
        val CATEGORIES_URI: Uri = Uri.parse("content://$AUTHORITY/categories")
        val TAGS_URI: Uri = Uri.parse("content://$AUTHORITY/tags")
        val TRANSACTIONS_URI: Uri = Uri.parse("content://$AUTHORITY/transactions")
        val LEGS_URI: Uri = Uri.parse("content://$AUTHORITY/legs")
        val TRANSACTION_TAGS_URI: Uri = Uri.parse("content://$AUTHORITY/transaction_tags")

        private const val ACCOUNTS = 1
        private const val CATEGORIES = 2
        private const val TAGS = 3
        private const val TRANSACTIONS = 4
        private const val LEGS = 5
        private const val TRANSACTION_TAGS = 6
        private const val INCOME_SOURCES = 7

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "accounts", ACCOUNTS)
            addURI(AUTHORITY, "income_sources", INCOME_SOURCES)
            addURI(AUTHORITY, "categories", CATEGORIES)
            addURI(AUTHORITY, "tags", TAGS)
            addURI(AUTHORITY, "transactions", TRANSACTIONS)
            addURI(AUTHORITY, "legs", LEGS)
            addURI(AUTHORITY, "transaction_tags", TRANSACTION_TAGS)
        }
    }
}
