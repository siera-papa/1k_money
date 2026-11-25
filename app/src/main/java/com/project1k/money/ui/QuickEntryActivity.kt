package com.project1k.money.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project1k.money.data.AccountEntity
import com.project1k.money.data.AccountType
import com.project1k.money.data.CategoryEntity
import com.project1k.money.data.CurrencyCatalog
import com.project1k.money.data.IncomeSourceEntity
import com.project1k.money.data.LoadedTransaction
import com.project1k.money.data.MoneyCodec
import com.project1k.money.data.TagEntity
import com.project1k.money.data.TransactionType
import kotlinx.coroutines.launch
import java.util.Calendar

class QuickEntryActivity : ComponentActivity() {
    private val vm by viewModels<MoneyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw the translucent quick-entry window behind the navigation/gesture area.
        // The sheet can therefore sit flush with the physical bottom edge, like 1Money,
        // instead of Android reserving an empty navigation-bar strip underneath it.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        val type = intent.getStringExtra(EXTRA_TYPE) ?: TransactionType.EXPENSE
        val transactionId = intent.getLongExtra(EXTRA_TRANSACTION_ID, 0L).takeIf { it > 0L }
        val fromWidget = intent.getBooleanExtra(EXTRA_FROM_WIDGET, false)

        setContent {
            Project1KMoneyTheme {
                val outsideClicks = remember { MutableInteractionSource() }
                val insideClicks = remember { MutableInteractionSource() }
                val close: () -> Unit = {
                    if (fromWidget) finishAndRemoveTask() else finish()
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = outsideClicks,
                            indication = null,
                            onClick = close
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 620.dp)
                            .clickable(
                                interactionSource = insideClicks,
                                indication = null,
                                onClick = {}
                            ),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        tonalElevation = 8.dp,
                        shadowElevation = 16.dp
                    ) {
                        QuickEntryScreen(
                            vm = vm,
                            initialType = type,
                            transactionId = transactionId,
                            onClose = close
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TYPE = "type"
        const val EXTRA_TRANSACTION_ID = "transaction_id"
        const val EXTRA_FROM_WIDGET = "from_widget"
    }
}

private enum class ActiveAmount { SOURCE, TARGET }

@Composable
private fun QuickEntryScreen(
    vm: MoneyViewModel,
    initialType: String,
    transactionId: Long?,
    onClose: () -> Unit
) {
    // Collect directly from Room with a nullable initial value. This prevents the old
    // one-frame "Add at least..." mini sheet while StateFlow still contained emptyList().
    val accountsOrNull by vm.repository.activeAccounts.collectAsState(initial = null)
    val categoriesOrNull by vm.repository.activeCategories.collectAsState(initial = null)
    val tagsOrNull by vm.repository.activeTags.collectAsState(initial = null)
    val incomeSourcesOrNull by vm.repository.activeIncomeSources.collectAsState(initial = null)

    if (accountsOrNull == null || categoriesOrNull == null || tagsOrNull == null || incomeSourcesOrNull == null) return

    val accounts = accountsOrNull.orEmpty()
    val categories = categoriesOrNull.orEmpty()
    val tags = tagsOrNull.orEmpty()
    val incomeSources = incomeSourcesOrNull.orEmpty()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var type by remember { mutableStateOf(initialType) }
    var amount by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    var activeAmount by remember { mutableStateOf(ActiveAmount.SOURCE) }
    var accountId by remember { mutableStateOf<Long?>(null) }
    var targetAccountId by remember { mutableStateOf<Long?>(null) }
    var incomeSourceId by remember { mutableStateOf<Long?>(null) }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var selectedTags by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var note by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    var suggestedTagIds by remember { mutableStateOf<List<Long>>(emptyList()) }
    var initialized by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var showTagPicker by remember { mutableStateOf(false) }

    val sourceAccounts = if (type == TransactionType.EXPENSE && transactionId == null) {
        accounts.filter { it.type == AccountType.PRIMARY }
    } else {
        accounts
    }
    val sourceAccount = sourceAccounts.firstOrNull { it.id == accountId }
    val targetAccount = accounts.firstOrNull { it.id == targetAccountId }
    val incomeSource = incomeSources.firstOrNull { it.id == incomeSourceId }
    val selectedCategory = categories.firstOrNull { it.id == categoryId }

    val sameCurrencyTransfer = type == TransactionType.TRANSFER &&
        sourceAccount != null && targetAccount != null &&
        sourceAccount.currency.equals(targetAccount.currency, ignoreCase = true)
    val sameCurrencyIncome = type == TransactionType.INCOME &&
        incomeSource != null && targetAccount != null &&
        incomeSource.currency.equals(targetAccount.currency, ignoreCase = true)

    val categoryById = categories.associateBy { it.id }
    fun categoryLabel(category: CategoryEntity?): String {
        if (category == null) return "Choose"
        val parent = category.parentId?.let { categoryById[it] }
        return if (parent == null) category.name else "${parent.name} › ${category.name}"
    }

    LaunchedEffect(transactionId, type, accounts, categories, incomeSources, tags, initialized) {
        if (initialized) return@LaunchedEffect

        if (transactionId != null) {
            val existing = vm.repository.loadTransaction(transactionId)
            if (existing != null) {
                type = existing.transaction.type
                timestamp = existing.transaction.timestamp
                note = existing.transaction.note
                categoryId = existing.transaction.categoryId
                incomeSourceId = existing.transaction.incomeSourceId
                selectedTags = existing.tagIds.filter { id -> tags.any { it.id == id } }.toSet()
                applyLoadedTransaction(
                    loaded = existing,
                    accounts = accounts,
                    incomeSources = incomeSources,
                    onSourceAccount = { id -> accountId = id },
                    onTargetAccount = { id -> targetAccountId = id },
                    onSourceAmount = { amount = it },
                    onTargetAmount = { targetAmount = it }
                )
            }
            initialized = true
            return@LaunchedEffect
        }

        val previous = vm.repository.loadLastTransaction(type)
        when (type) {
            TransactionType.EXPENSE -> {
                val previousAccount = previous?.legs?.firstOrNull { it.amountAtomic < 0 }?.accountId
                accountId = previousAccount?.takeIf { id -> sourceAccounts.any { it.id == id } }
                    ?: sourceAccounts.firstOrNull()?.id
                categoryId = previous?.transaction?.categoryId?.takeIf { id -> categories.any { it.id == id } }
                    ?: categories.firstOrNull()?.id
                selectedTags = previous?.tagIds?.filter { id -> tags.any { it.id == id } }?.toSet().orEmpty()
            }
            TransactionType.TRANSFER -> {
                val previousFrom = previous?.legs?.firstOrNull { it.amountAtomic < 0 }?.accountId
                val previousTo = previous?.legs?.firstOrNull { it.amountAtomic > 0 }?.accountId
                accountId = previousFrom?.takeIf { id -> accounts.any { it.id == id } }
                    ?: accounts.firstOrNull()?.id
                targetAccountId = previousTo?.takeIf { id -> accounts.any { it.id == id && it.id != accountId } }
                    ?: accounts.firstOrNull { it.id != accountId }?.id
                selectedTags = previous?.tagIds?.filter { id -> tags.any { it.id == id } }?.toSet().orEmpty()
            }
            TransactionType.INCOME -> {
                incomeSourceId = previous?.transaction?.incomeSourceId
                    ?.takeIf { id -> incomeSources.any { it.id == id } }
                    ?: incomeSources.firstOrNull()?.id
                val previousAccount = previous?.legs?.firstOrNull { it.amountAtomic > 0 }?.accountId
                targetAccountId = previousAccount?.takeIf { id -> accounts.any { it.id == id } }
                    ?: accounts.firstOrNull()?.id
                selectedTags = previous?.tagIds?.filter { id -> tags.any { it.id == id } }?.toSet().orEmpty()
            }
        }
        initialized = true
    }

    LaunchedEffect(categoryId, tags) {
        suggestedTagIds = vm.repository.suggestedTags(if (type == TransactionType.EXPENSE) categoryId else null).map { it.id }
    }

    LaunchedEffect(sameCurrencyTransfer, sameCurrencyIncome, amount) {
        if (sameCurrencyTransfer || sameCurrencyIncome) targetAmount = amount
    }

    fun updateSourceAmount(newValue: String) {
        amount = newValue
        if (sameCurrencyTransfer || sameCurrencyIncome) targetAmount = newValue
    }

    val sourceCurrency = when (type) {
        TransactionType.INCOME -> incomeSource?.currency
        else -> sourceAccount?.currency
    }
    val destinationCurrency = when (type) {
        TransactionType.TRANSFER, TransactionType.INCOME -> targetAccount?.currency
        else -> null
    }

    val sourceAtomic = sourceCurrency?.let { MoneyCodec.parseAtomic(amount, it) }
    val targetAtomic = when {
        type == TransactionType.EXPENSE -> null
        type == TransactionType.TRANSFER && sameCurrencyTransfer -> sourceAtomic
        type == TransactionType.INCOME && sameCurrencyIncome -> sourceAtomic
        else -> destinationCurrency?.let { MoneyCodec.parseAtomic(targetAmount, it) }
    }

    val canSave = when (type) {
        TransactionType.EXPENSE -> sourceAccount != null && selectedCategory != null && sourceAtomic != null && sourceAtomic > 0
        TransactionType.TRANSFER -> sourceAccount != null && targetAccount != null && sourceAccount.id != targetAccount.id &&
            sourceAtomic != null && sourceAtomic > 0 && targetAtomic != null && targetAtomic > 0
        TransactionType.INCOME -> incomeSource != null && targetAccount != null &&
            sourceAtomic != null && sourceAtomic > 0 && targetAtomic != null && targetAtomic > 0
        else -> false
    }

    fun showDatePicker() {
        val selected = Calendar.getInstance().apply { timeInMillis = timestamp }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                timestamp = Calendar.getInstance().apply {
                    timeInMillis = timestamp
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.timeInMillis
            },
            selected.get(Calendar.YEAR),
            selected.get(Calendar.MONTH),
            selected.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun saveTransaction() {
        if (!canSave) return
        error = null
        scope.launch {
            runCatching {
                when (type) {
                    TransactionType.EXPENSE -> vm.repository.saveExpense(
                        existingId = transactionId,
                        timestamp = timestamp,
                        accountId = requireNotNull(sourceAccount).id,
                        amountAtomic = requireNotNull(sourceAtomic),
                        categoryId = requireNotNull(categoryId),
                        tagIds = selectedTags,
                        note = note.trim()
                    )
                    TransactionType.TRANSFER -> vm.repository.saveTransfer(
                        existingId = transactionId,
                        timestamp = timestamp,
                        fromAccountId = requireNotNull(sourceAccount).id,
                        toAccountId = requireNotNull(targetAccount).id,
                        amountFromAtomic = requireNotNull(sourceAtomic),
                        amountToAtomic = requireNotNull(targetAtomic),
                        tagIds = selectedTags,
                        note = note.trim()
                    )
                    TransactionType.INCOME -> vm.repository.saveIncome(
                        existingId = transactionId,
                        timestamp = timestamp,
                        incomeSourceId = requireNotNull(incomeSource).id,
                        sourceAmountAtomic = requireNotNull(sourceAtomic),
                        toAccountId = requireNotNull(targetAccount).id,
                        receivedAmountAtomic = requireNotNull(targetAtomic),
                        tagIds = selectedTags,
                        note = note.trim()
                    )
                    else -> error("Unknown transaction type")
                }
            }.onSuccess { onClose() }
                .onFailure { error = it.message ?: "Could not save transaction" }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        when (type) {
            TransactionType.EXPENSE -> {
                Row(Modifier.fillMaxWidth()) {
                    CompactAccountSelector(
                        label = "From account",
                        accounts = sourceAccounts,
                        selectedId = accountId,
                        modifier = Modifier.weight(1f),
                        onSelect = { accountId = it }
                    )
                    CompactCategorySelector(
                        label = "To category",
                        categories = categories,
                        selectedId = categoryId,
                        display = categoryLabel(selectedCategory),
                        modifier = Modifier.weight(1f),
                        onSelect = { categoryId = it }
                    )
                }
                if (sourceAccounts.isEmpty() && transactionId == null) {
                    Text(
                        "Add a primary account in Settings to enter an expense.",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                AmountPanel(
                    label = null,
                    currency = sourceAccount?.currency.orEmpty(),
                    value = amount,
                    selected = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = { activeAmount = ActiveAmount.SOURCE }
                )
            }

            TransactionType.TRANSFER -> {
                Row(Modifier.fillMaxWidth()) {
                    CompactAccountSelector(
                        label = "From account",
                        accounts = accounts,
                        selectedId = accountId,
                        modifier = Modifier.weight(1f),
                        onSelect = { id ->
                            accountId = id
                            if (targetAccountId == id) targetAccountId = accounts.firstOrNull { it.id != id }?.id
                        }
                    )
                    CompactAccountSelector(
                        label = "To account",
                        accounts = accounts.filter { it.id != accountId },
                        selectedId = targetAccountId,
                        modifier = Modifier.weight(1f),
                        onSelect = { targetAccountId = it }
                    )
                }
                if (sameCurrencyTransfer) {
                    AmountPanel(
                        label = null,
                        currency = sourceAccount?.currency.orEmpty(),
                        value = amount,
                        selected = true,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = { activeAmount = ActiveAmount.SOURCE }
                    )
                } else {
                    DualAmountPanels(
                        leftLabel = "Sent",
                        leftCurrency = sourceAccount?.currency.orEmpty(),
                        leftValue = amount,
                        rightLabel = "Received",
                        rightCurrency = targetAccount?.currency.orEmpty(),
                        rightValue = targetAmount,
                        activeAmount = activeAmount,
                        onActive = { activeAmount = it }
                    )
                }
            }

            TransactionType.INCOME -> {
                Row(Modifier.fillMaxWidth()) {
                    CompactIncomeSourceSelector(
                        label = "From source",
                        sources = incomeSources,
                        selectedId = incomeSourceId,
                        modifier = Modifier.weight(1f),
                        onSelect = { incomeSourceId = it }
                    )
                    CompactAccountSelector(
                        label = "To account",
                        accounts = accounts,
                        selectedId = targetAccountId,
                        modifier = Modifier.weight(1f),
                        onSelect = { targetAccountId = it }
                    )
                }
                if (sameCurrencyIncome) {
                    AmountPanel(
                        label = null,
                        currency = incomeSource?.currency.orEmpty(),
                        value = amount,
                        selected = true,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        onClick = { activeAmount = ActiveAmount.SOURCE }
                    )
                } else {
                    DualAmountPanels(
                        leftLabel = "Source",
                        leftCurrency = incomeSource?.currency.orEmpty(),
                        leftValue = amount,
                        rightLabel = "Received",
                        rightCurrency = targetAccount?.currency.orEmpty(),
                        rightValue = targetAmount,
                        activeAmount = activeAmount,
                        onActive = { activeAmount = it }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val selectedTagObjects = selectedTags.mapNotNull { id -> tags.firstOrNull { it.id == id } }
            TextButton(onClick = { showTagPicker = true }) {
                Text(if (selectedTags.isEmpty()) "Tags +" else "Tags (${selectedTags.size})")
            }
            if (selectedTagObjects.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(selectedTagObjects, key = { it.id }) { tag ->
                        FilterChip(
                            selected = true,
                            onClick = { selectedTags = selectedTags - tag.id },
                            label = { Text("#${tag.name}") }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = { Text("Note (optional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        if (error != null) {
            Text(
                error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        NumberPad(
            enabled = canSave,
            onKey = { key ->
                val activeCurrency = when {
                    (type == TransactionType.TRANSFER || type == TransactionType.INCOME) &&
                        activeAmount == ActiveAmount.TARGET &&
                        !(sameCurrencyTransfer || sameCurrencyIncome) -> destinationCurrency
                    else -> sourceCurrency
                }.orEmpty()
                val scale = MoneyCodec.scale(activeCurrency)
                if (activeAmount == ActiveAmount.TARGET && !(sameCurrencyTransfer || sameCurrencyIncome)) {
                    targetAmount = if (key == "⌫") targetAmount.dropLast(1) else appendAmount(targetAmount, key, scale)
                } else {
                    updateSourceAmount(if (key == "⌫") amount.dropLast(1) else appendAmount(amount, key, scale))
                }
            },
            onDate = ::showDatePicker,
            onSave = ::saveTransaction
        )

        Text(
            text = formatAppDate(timestamp),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (showTagPicker) {
        TagPickerDialog(
            tags = tags,
            suggestedIds = suggestedTagIds,
            selected = selectedTags,
            onSelectedChange = { selectedTags = it },
            onAddTag = { name ->
                scope.launch {
                    val id = vm.repository.saveTag(TagEntity(name = name.trim()))
                    selectedTags = selectedTags + id
                }
            },
            onDismiss = { showTagPicker = false }
        )
    }
}

@Composable
private fun CompactAccountSelector(
    label: String,
    accounts: List<AccountEntity>,
    selectedId: Long?,
    modifier: Modifier = Modifier,
    onSelect: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = accounts.firstOrNull { it.id == selectedId }
    Box(modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { if (accounts.isNotEmpty()) expanded = true },
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(
                    selected?.name ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(selected?.let { CurrencyCatalog.label(it.currency) }.orEmpty(), style = MaterialTheme.typography.bodySmall)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            accounts.forEach { account ->
                DropdownMenuItem(
                    text = { Text("${account.name} · ${CurrencyCatalog.label(account.currency)}") },
                    onClick = { expanded = false; onSelect(account.id) }
                )
            }
        }
    }
}

@Composable
private fun CompactIncomeSourceSelector(
    label: String,
    sources: List<IncomeSourceEntity>,
    selectedId: Long?,
    modifier: Modifier = Modifier,
    onSelect: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = sources.firstOrNull { it.id == selectedId }
    Box(modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { if (sources.isNotEmpty()) expanded = true },
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(
                    selected?.name ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(selected?.let { CurrencyCatalog.label(it.currency) }.orEmpty(), style = MaterialTheme.typography.bodySmall)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            sources.forEach { source ->
                DropdownMenuItem(
                    text = { Text("${source.name} · ${CurrencyCatalog.label(source.currency)}") },
                    onClick = { expanded = false; onSelect(source.id) }
                )
            }
        }
    }
}

@Composable
private fun CompactCategorySelector(
    label: String,
    categories: List<CategoryEntity>,
    selectedId: Long?,
    display: String,
    modifier: Modifier = Modifier,
    onSelect: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categoryById = categories.associateBy { it.id }
    Box(modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { if (categories.isNotEmpty()) expanded = true },
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(display, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Category", style = MaterialTheme.typography.bodySmall)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                val parent = category.parentId?.let { categoryById[it] }
                val name = if (parent == null) category.name else "${parent.name} › ${category.name}"
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = { expanded = false; onSelect(category.id) }
                )
            }
        }
    }
}

@Composable
private fun DualAmountPanels(
    leftLabel: String,
    leftCurrency: String,
    leftValue: String,
    rightLabel: String,
    rightCurrency: String,
    rightValue: String,
    activeAmount: ActiveAmount,
    onActive: (ActiveAmount) -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AmountPanel(
            label = leftLabel,
            currency = leftCurrency,
            value = leftValue,
            selected = activeAmount == ActiveAmount.SOURCE,
            modifier = Modifier.weight(1f),
            onClick = { onActive(ActiveAmount.SOURCE) }
        )
        AmountPanel(
            label = rightLabel,
            currency = rightCurrency,
            value = rightValue,
            selected = activeAmount == ActiveAmount.TARGET,
            modifier = Modifier.weight(1f),
            onClick = { onActive(ActiveAmount.TARGET) }
        )
    }
}

@Composable
private fun AmountPanel(
    label: String?,
    currency: String,
    value: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (label != null) Text(label, style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    CurrencyCatalog.symbol(currency),
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    value.ifBlank { "0" },
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun NumberPad(enabled: Boolean, onKey: (String) -> Unit, onDate: () -> Unit, onSave: () -> Unit) {
    val rows = listOf(
        listOf("7", "8", "9"),
        listOf("4", "5", "6"),
        listOf("1", "2", "3"),
        listOf("00", "0", ".")
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(226.dp).padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Column(
            modifier = Modifier.weight(3f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { key ->
                        OutlinedButton(
                            onClick = { onKey(key) },
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(13.dp)
                        ) {
                            Text(key, fontSize = 23.sp)
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedButton(
                onClick = { onKey("⌫") },
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text("⌫", fontSize = 21.sp)
            }
            OutlinedButton(
                onClick = onDate,
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Choose date"
                )
            }
            Button(
                onClick = onSave,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().weight(2f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Text("✓", fontSize = 32.sp)
            }
        }
    }
}

@Composable
private fun TagPickerDialog(
    tags: List<TagEntity>,
    suggestedIds: List<Long>,
    selected: Set<Long>,
    onSelectedChange: (Set<Long>) -> Unit,
    onAddTag: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newTag by remember { mutableStateOf("") }
    val ordered = (suggestedIds + tags.map { it.id })
        .distinct()
        .mapNotNull { id -> tags.firstOrNull { it.id == id } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tags") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newTag,
                        onValueChange = { newTag = it },
                        label = { Text("New tag") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            val clean = newTag.trim().removePrefix("#")
                            if (clean.isNotBlank()) {
                                onAddTag(clean)
                                newTag = ""
                            }
                        }
                    ) { Text("Add") }
                }
                LazyColumn(Modifier.heightIn(max = 340.dp)) {
                    items(ordered, key = { it.id }) { tag ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectedChange(if (tag.id in selected) selected - tag.id else selected + tag.id)
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = tag.id in selected,
                                onCheckedChange = {
                                    onSelectedChange(if (tag.id in selected) selected - tag.id else selected + tag.id)
                                }
                            )
                            Text("#${tag.name}")
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } }
    )
}

private fun appendAmount(current: String, key: String, scale: Int): String {
    if (key == ".") {
        if (scale == 0 || current.contains('.')) return current
        return if (current.isBlank()) "0." else "$current."
    }
    if (key != "00" && key.any { !it.isDigit() }) return current
    if (key == "00" && current.isBlank()) return "0"
    val decimal = current.substringAfter('.', "")
    if (current.contains('.') && decimal.length + key.length > scale) return current
    if (current == "0" && key != "00") return key
    return current + key
}

private fun applyLoadedTransaction(
    loaded: LoadedTransaction,
    accounts: List<AccountEntity>,
    incomeSources: List<IncomeSourceEntity>,
    onSourceAccount: (Long) -> Unit,
    onTargetAccount: (Long) -> Unit,
    onSourceAmount: (String) -> Unit,
    onTargetAmount: (String) -> Unit
) {
    val tx = loaded.transaction
    when (tx.type) {
        TransactionType.TRANSFER -> {
            val from = loaded.legs.firstOrNull { it.amountAtomic < 0 }
            val to = loaded.legs.firstOrNull { it.amountAtomic > 0 }
            if (from != null) {
                accounts.firstOrNull { it.id == from.accountId }?.let { account ->
                    onSourceAccount(from.accountId)
                    onSourceAmount(MoneyCodec.formatAtomic(-from.amountAtomic, account.currency))
                }
            }
            if (to != null) {
                accounts.firstOrNull { it.id == to.accountId }?.let { account ->
                    onTargetAccount(to.accountId)
                    onTargetAmount(MoneyCodec.formatAtomic(to.amountAtomic, account.currency))
                }
            }
        }
        TransactionType.INCOME -> {
            val to = loaded.legs.firstOrNull { it.amountAtomic > 0 }
            val source = tx.incomeSourceId?.let { id -> incomeSources.firstOrNull { it.id == id } }
            if (source != null && tx.incomeSourceAmountAtomic != null) {
                onSourceAmount(MoneyCodec.formatAtomic(tx.incomeSourceAmountAtomic, source.currency))
            }
            if (to != null) {
                accounts.firstOrNull { it.id == to.accountId }?.let { account ->
                    onTargetAccount(to.accountId)
                    onTargetAmount(MoneyCodec.formatAtomic(to.amountAtomic, account.currency))
                }
            }
        }
        else -> {
            val leg = loaded.legs.firstOrNull()
            if (leg != null) {
                accounts.firstOrNull { it.id == leg.accountId }?.let { account ->
                    onSourceAccount(leg.accountId)
                    onSourceAmount(MoneyCodec.formatAtomic(kotlin.math.abs(leg.amountAtomic), account.currency))
                }
            }
        }
    }
}
