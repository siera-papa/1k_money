package com.project1k.money.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

object TransactionType {
    const val EXPENSE = "EXPENSE"
    const val INCOME = "INCOME"
    const val TRANSFER = "TRANSFER"
}

object AccountType {
    const val PRIMARY = "primary"
    const val SAVING = "saving"
    const val BETTING = "betting"
    val all = listOf(PRIMARY, SAVING, BETTING)
}

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val currency: String,
    val openingBalanceAtomic: Long,
    val openingDate: Long,
    val groupLabel: String? = null,
    val trackerId: String? = null,
    val archived: Boolean = false,
    @ColumnInfo(defaultValue = "'primary'") val type: String = AccountType.PRIMARY
)

@Entity(tableName = "income_sources")
data class IncomeSourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val currency: String,
    val archived: Boolean = false
)

@Entity(
    tableName = "categories",
    indices = [Index("parentId")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val parentId: Long? = null,
    val pinned: Boolean = false,
    val archived: Boolean = false
)

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val pinned: Boolean = false,
    val archived: Boolean = false
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId"), Index("incomeSourceId"), Index("timestamp")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val timestamp: Long,
    val categoryId: Long? = null,
    val incomeSourceId: Long? = null,
    val incomeSourceAmountAtomic: Long? = null,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "transaction_legs",
    primaryKeys = ["transactionId", "accountId"],
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("transactionId"), Index("accountId")]
)
data class TransactionLegEntity(
    val transactionId: Long,
    val accountId: Long,
    val amountAtomic: Long
)

@Entity(
    tableName = "transaction_tags",
    primaryKeys = ["transactionId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("transactionId"), Index("tagId")]
)
data class TransactionTagEntity(
    val transactionId: Long,
    val tagId: Long
)

data class CategoryUsage(
    val id: Long,
    val name: String,
    val parentId: Long?,
    val pinned: Boolean,
    val archived: Boolean,
    val lastUsed: Long?,
    val useCount: Long
)

data class TagUsage(
    val id: Long,
    val name: String,
    val pinned: Boolean,
    val archived: Boolean,
    val lastUsed: Long?,
    val useCount: Long
)
