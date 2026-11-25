package com.project1k.money.ui

import android.content.Intent
import java.time.Instant
import java.time.ZoneId
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project1k.money.MoneyApplication
import com.project1k.money.data.AccountEntity
import com.project1k.money.data.AccountType
import com.project1k.money.data.CategoryEntity
import com.project1k.money.data.CurrencyCatalog
import com.project1k.money.data.IncomeSourceEntity
import com.project1k.money.data.MoneyCodec
import com.project1k.money.data.TagEntity
import com.project1k.money.data.TransactionType

private enum class MainTab { TRANSACTIONS, CLASSIFICATION, CALC, SETTINGS }

@Composable
fun MoneyApp(vm: MoneyViewModel) {
    MaterialTheme {
        var tab by remember { mutableStateOf(MainTab.TRANSACTIONS) }
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = tab == MainTab.TRANSACTIONS,
                        onClick = { tab = MainTab.TRANSACTIONS },
                        icon = { Icon(Icons.Default.ReceiptLong, null) },
                        label = { Text("Transactions") }
                    )
                    NavigationBarItem(
                        selected = tab == MainTab.CLASSIFICATION,
                        onClick = { tab = MainTab.CLASSIFICATION },
                        icon = { Icon(Icons.Default.Label, null) },
                        label = { Text("Categories & Tags") }
                    )
                    NavigationBarItem(
                        selected = tab == MainTab.CALC,
                        onClick = { tab = MainTab.CALC },
                        icon = { Icon(Icons.Default.Calculate, null) },
                        label = { Text("Calc") }
                    )
                    NavigationBarItem(
                        selected = tab == MainTab.SETTINGS,
                        onClick = { tab = MainTab.SETTINGS },
                        icon = { Icon(Icons.Default.Settings, null) },
                        label = { Text("Settings") }
                    )
                }
            }
        ) { padding ->
            Surface(Modifier.fillMaxSize().padding(padding)) {
                when (tab) {
                    MainTab.TRANSACTIONS -> TransactionsScreen(vm)
                    MainTab.CLASSIFICATION -> ClassificationScreen(vm)
                    MainTab.CALC -> {
                        val app = LocalContext.current.applicationContext as MoneyApplication
                        CalcScreen(store = app.cashCalcStore)
                    }
                    MainTab.SETTINGS -> SettingsScreen(vm)
                }
            }
        }
    }
}

@Composable
private fun TransactionsScreen(vm: MoneyViewModel) {
    val context = LocalContext.current
    val rows by vm.transactionRows.collectAsState()
    val zone = ZoneId.systemDefault()
    val dateGroups = remember(rows, zone) {
        rows.sortedByDescending { it.timestamp }.groupBy {
            Instant.ofEpochMilli(it.timestamp).atZone(zone).toLocalDate()
        }
    }
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) { Text("+", fontSize = 36.sp, fontWeight = FontWeight.Light) }
        }
    ) { padding ->
        if (rows.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No transactions yet", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Daily entry is designed to happen from the home-screen widget.")
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                dateGroups.forEach { (day, dayRows) ->
                    item(key = "date_$day") {
                        Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text(
                                text = formatAppDate(dayRows.first().timestamp),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    items(dayRows, key = { it.id }) { row ->
                        TransactionRowItem(
                            row = row,
                            onEdit = {
                                context.startActivity(
                                    Intent(context, QuickEntryActivity::class.java)
                                        .putExtra(QuickEntryActivity.EXTRA_TYPE, row.type)
                                        .putExtra(QuickEntryActivity.EXTRA_TRANSACTION_ID, row.id)
                                )
                            },
                            onDelete = { vm.deleteTransaction(row.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }

    if (showAdd) {
        OperationChooser(
            onDismiss = { showAdd = false },
            onPick = { type ->
                showAdd = false
                context.startActivity(
                    Intent(context, QuickEntryActivity::class.java)
                        .putExtra(QuickEntryActivity.EXTRA_TYPE, type)
                )
            }
        )
    }
}

@Composable
private fun TransactionRowItem(row: TransactionRow, onEdit: () -> Unit, onDelete: () -> Unit) {
    val date = remember(row.timestamp) { formatAppTime(row.timestamp) }
    val primary = row.legs.firstOrNull()
    val title = when (row.type) {
        TransactionType.EXPENSE -> row.categoryName ?: "Expense"
        TransactionType.INCOME -> row.incomeSourceName ?: "Income"
        else -> {
            val from = row.legs.firstOrNull { it.amountAtomic < 0 }?.accountName ?: "?"
            val to = row.legs.firstOrNull { it.amountAtomic > 0 }?.accountName ?: "?"
            "$from → $to"
        }
    }
    val amountText = when (row.type) {
        TransactionType.TRANSFER -> {
            val from = row.legs.firstOrNull { it.amountAtomic < 0 }
            val to = row.legs.firstOrNull { it.amountAtomic > 0 }
            if (from != null && to != null) {
                "${MoneyCodec.formatWithCurrency(-from.amountAtomic, from.currency)} → ${MoneyCodec.formatWithCurrency(to.amountAtomic, to.currency)}"
            } else "Transfer"
        }
        TransactionType.INCOME -> {
            val received = primary
            val sourceAmount = row.incomeSourceAmountAtomic
            val sourceCurrency = row.incomeSourceCurrency
            if (received != null && sourceAmount != null && sourceCurrency != null && !sourceCurrency.equals(received.currency, ignoreCase = true)) {
                "${MoneyCodec.formatWithCurrency(sourceAmount, sourceCurrency)} → ${MoneyCodec.formatWithCurrency(received.amountAtomic, received.currency)}"
            } else {
                received?.let { MoneyCodec.formatWithCurrency(kotlin.math.abs(it.amountAtomic), it.currency) } ?: ""
            }
        }
        else -> primary?.let { MoneyCodec.formatWithCurrency(kotlin.math.abs(it.amountAtomic), it.currency) } ?: ""
    }

    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEdit).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(amountText)
            Text(date, style = MaterialTheme.typography.bodySmall)
            if (row.tags.isNotEmpty()) Text(row.tags.joinToString("  ") { "#$it" }, style = MaterialTheme.typography.bodySmall)
            if (row.note.isNotBlank()) Text(row.note, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
    }
}

@Composable
private fun OperationChooser(onDismiss: () -> Unit, onPick: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New transaction") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onPick(TransactionType.EXPENSE) }, modifier = Modifier.fillMaxWidth()) { Text("Expense") }
                Button(onClick = { onPick(TransactionType.TRANSFER) }, modifier = Modifier.fillMaxWidth()) { Text("Transfer") }
                Button(onClick = { onPick(TransactionType.INCOME) }, modifier = Modifier.fillMaxWidth()) { Text("Income") }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ClassificationScreen(vm: MoneyViewModel) {
    val categories by vm.categories.collectAsState()
    val tags by vm.tags.collectAsState()
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var editingTag by remember { mutableStateOf<TagEntity?>(null) }
    var newCategory by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(false) }

    val roots = categories.filter { it.parentId == null }
    val childrenByParent = categories.filter { it.parentId != null }.groupBy { it.parentId }

    LazyColumn(Modifier.fillMaxSize()) {
        item { SectionHeader("Categories") { newCategory = true } }
        roots.forEach { root ->
            item(key = "c${root.id}") {
                ClassificationRow(root.name, root.pinned, root.archived) { editingCategory = root }
            }
            val children = childrenByParent[root.id].orEmpty()
            items(children, key = { "c${it.id}" }) { child ->
                ClassificationRow("    ↳ ${child.name}", child.pinned, child.archived) { editingCategory = child }
            }
        }
        // Defensive: show any orphaned child if an old/imported parent is missing.
        val rootIds = roots.map { it.id }.toSet()
        items(categories.filter { it.parentId != null && it.parentId !in rootIds }, key = { "orphan${it.id}" }) { category ->
            ClassificationRow("    ↳ ${category.name}", category.pinned, category.archived) { editingCategory = category }
        }
        item {
            Spacer(Modifier.height(18.dp))
            SectionHeader("Tags") { newTag = true }
        }
        items(tags, key = { "t${it.id}" }) { tag ->
            ClassificationRow("#${tag.name}", tag.pinned, tag.archived) { editingTag = tag }
        }
        item { Spacer(Modifier.height(32.dp)) }
    }

    if (newCategory || editingCategory != null) {
        CategoryEditDialog(
            category = editingCategory,
            allCategories = categories,
            onDismiss = { newCategory = false; editingCategory = null },
            onSave = { name, parentId, pinned, archived ->
                vm.saveCategory(editingCategory, name, parentId, pinned, archived)
                newCategory = false
                editingCategory = null
            }
        )
    }

    if (newTag || editingTag != null) {
        ClassificationEditDialog(
            title = if (editingTag == null) "New tag" else "Edit tag",
            initialName = editingTag?.name.orEmpty(),
            initialPinned = editingTag?.pinned ?: false,
            initialArchived = editingTag?.archived ?: false,
            onDismiss = { newTag = false; editingTag = null },
            onSave = { name, pinned, archived ->
                vm.saveTag(editingTag, name, pinned, archived)
                newTag = false
                editingTag = null
            }
        )
    }
}

@Composable
private fun SectionHeader(title: String, onAdd: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        OutlinedButton(onClick = onAdd) { Text("Add") }
    }
}

@Composable
private fun ClassificationRow(name: String, pinned: Boolean, archived: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, modifier = Modifier.weight(1f))
        if (pinned) Text("Pinned", style = MaterialTheme.typography.bodySmall)
        if (archived) Text("  Archived", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CategoryEditDialog(
    category: CategoryEntity?,
    allCategories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Long?, Boolean, Boolean) -> Unit
) {
    var name by remember(category?.id) { mutableStateOf(category?.name.orEmpty()) }
    var parentId by remember(category?.id) { mutableStateOf(category?.parentId) }
    var pinned by remember(category?.id) { mutableStateOf(category?.pinned ?: false) }
    var archived by remember(category?.id) { mutableStateOf(category?.archived ?: false) }
    var parentMenu by remember { mutableStateOf(false) }

    // Only top-level categories can be parents, which keeps the hierarchy at exactly two levels.
    val parentChoices = allCategories.filter { it.parentId == null && it.id != category?.id }
    val selectedParent = parentChoices.firstOrNull { it.id == parentId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "New category" else "Edit category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
                Box {
                    OutlinedTextField(
                        value = selectedParent?.name ?: "Top level",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Parent category") },
                        trailingIcon = { TextButton(onClick = { parentMenu = true }) { Text("Change") } }
                    )
                    DropdownMenu(expanded = parentMenu, onDismissRequest = { parentMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Top level") },
                            onClick = { parentId = null; parentMenu = false }
                        )
                        parentChoices.forEach { parent ->
                            DropdownMenuItem(
                                text = { Text(parent.name) },
                                onClick = { parentId = parent.id; parentMenu = false }
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(pinned, { pinned = it })
                    Text("Pin near the top")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(archived, { archived = it })
                    Text("Archived")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, parentId, pinned, archived) },
                enabled = name.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun ClassificationEditDialog(
    title: String,
    initialName: String,
    initialPinned: Boolean,
    initialArchived: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Boolean, Boolean) -> Unit
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var pinned by remember(initialPinned) { mutableStateOf(initialPinned) }
    var archived by remember(initialArchived) { mutableStateOf(initialArchived) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(pinned, { pinned = it })
                    Text("Pin near the top")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(archived, { archived = it })
                    Text("Archived")
                }
            }
        },
        confirmButton = { TextButton(onClick = { onSave(name, pinned, archived) }, enabled = name.isNotBlank()) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SettingsScreen(vm: MoneyViewModel) {
    val accounts by vm.accounts.collectAsState()
    val incomeSources by vm.incomeSources.collectAsState()
    val backupUi by vm.backupUi.collectAsState()
    var editingAccount by remember { mutableStateOf<AccountEntity?>(null) }
    var newAccount by remember { mutableStateOf(false) }
    var editingIncomeSource by remember { mutableStateOf<IncomeSourceEntity?>(null) }
    var newIncomeSource by remember { mutableStateOf(false) }
    var confirmRestore by remember { mutableStateOf(false) }

    val folderPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) vm.setBackupFolder(uri)
    }
    val restorePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) vm.restoreBackup(uri)
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            SectionHeader("Accounts") { newAccount = true }
            Text(
                "Opening balance is shown here only as setup feedback. Current per-account balances are intentionally not displayed.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        items(accounts, key = { "a${it.id}" }) { account ->
            Row(
                Modifier.fillMaxWidth().clickable { editingAccount = account }.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(account.name, fontWeight = FontWeight.SemiBold)
                    Text("Type: ${account.type}", style = MaterialTheme.typography.bodySmall)
                    Text(CurrencyCatalog.label(account.currency), style = MaterialTheme.typography.bodySmall)
                    account.trackerId?.let { Text("Tracker ID: $it", style = MaterialTheme.typography.bodySmall) }
                }
                Text(MoneyCodec.formatWithCurrency(account.openingBalanceAtomic, account.currency))
                if (account.archived) Text("  Archived", style = MaterialTheme.typography.bodySmall)
            }
            Divider()
        }

        item {
            Spacer(Modifier.height(18.dp))
            SectionHeader("Income sources") { newIncomeSource = true }
            Text(
                "Income sources have a currency but no balance. They are the left side of an income entry.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        items(incomeSources, key = { "i${it.id}" }) { source ->
            Row(
                Modifier.fillMaxWidth().clickable { editingIncomeSource = source }.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(source.name, fontWeight = FontWeight.SemiBold)
                    Text(CurrencyCatalog.label(source.currency), style = MaterialTheme.typography.bodySmall)
                }
                if (source.archived) Text("Archived", style = MaterialTheme.typography.bodySmall)
            }
            Divider()
        }

        item {
            Spacer(Modifier.height(22.dp))
            Text("Local backup & restore", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(8.dp))
            Text(
                if (backupUi.folderConfigured) "Backup folder: ${backupUi.folderLabel ?: "Selected folder"}" else "Backup folder: not selected",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "After a folder is selected, an automatic backup is created about once per day and continues after phone restarts. The latest 14 automatic backups are kept; manual and pre-restore backups are not deleted automatically.",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { folderPicker.launch(null) },
                    enabled = !backupUi.busy,
                    modifier = Modifier.weight(1f)
                ) { Text(if (backupUi.folderConfigured) "Change folder" else "Choose folder") }
                Button(
                    onClick = vm::backupNow,
                    enabled = backupUi.folderConfigured && !backupUi.busy,
                    modifier = Modifier.weight(1f)
                ) { Text("Backup now") }
            }
            OutlinedButton(
                onClick = { confirmRestore = true },
                enabled = !backupUi.busy,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
            ) { Text("Restore from backup file") }
            if (backupUi.busy) {
                Text("Working…", modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
            }
            backupUi.status?.let { status ->
                Text(status, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(32.dp))
        }
    }

    if (newAccount || editingAccount != null) {
        AccountEditDialog(
            account = editingAccount,
            onDismiss = { newAccount = false; editingAccount = null },
            onSave = { name, type, currency, opening, trackerId, archived ->
                vm.saveAccount(editingAccount, name, type, currency, opening, trackerId, archived)
                newAccount = false
                editingAccount = null
            }
        )
    }

    if (newIncomeSource || editingIncomeSource != null) {
        IncomeSourceEditDialog(
            source = editingIncomeSource,
            onDismiss = { newIncomeSource = false; editingIncomeSource = null },
            onSave = { name, currency, archived ->
                vm.saveIncomeSource(editingIncomeSource, name, currency, archived)
                newIncomeSource = false
                editingIncomeSource = null
            }
        )
    }

    if (confirmRestore) {
        AlertDialog(
            onDismissRequest = { confirmRestore = false },
            title = { Text("Restore backup?") },
            text = { Text("Restore replaces all current Money data. If a backup folder is configured, the app first creates a safety backup of the current data.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmRestore = false
                    restorePicker.launch(arrayOf("application/json", "text/plain", "application/octet-stream"))
                }) { Text("Choose backup") }
            },
            dismissButton = { TextButton(onClick = { confirmRestore = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun CurrencyPicker(
    currency: String,
    onCurrencyChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = CurrencyCatalog.pickerLabel(currency),
            onValueChange = {},
            readOnly = true,
            label = { Text("Currency") },
            trailingIcon = { TextButton(onClick = { expanded = true }) { Text("Change") } },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            CurrencyCatalog.all.forEach { spec ->
                DropdownMenuItem(
                    text = { Text("${spec.displayName} (${spec.code})") },
                    onClick = {
                        onCurrencyChange(spec.code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountTypePicker(
    type: String,
    onTypeChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = type,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type") },
            trailingIcon = { TextButton(onClick = { expanded = true }) { Text("Change") } },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            AccountType.all.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTypeChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountEditDialog(
    account: AccountEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, Boolean) -> Unit
) {
    var name by remember(account?.id) { mutableStateOf(account?.name.orEmpty()) }
    var type by remember(account?.id) { mutableStateOf(account?.type ?: AccountType.PRIMARY) }
    var currency by remember(account?.id) { mutableStateOf(account?.currency ?: "RSD") }
    var opening by remember(account?.id) {
        mutableStateOf(account?.let { MoneyCodec.formatAtomic(it.openingBalanceAtomic, it.currency) } ?: "0")
    }
    var trackerId by remember(account?.id) { mutableStateOf(account?.trackerId.orEmpty()) }
    var archived by remember(account?.id) { mutableStateOf(account?.archived ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (account == null) "New account" else "Edit account") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                AccountTypePicker(type = type, onTypeChange = { type = it })
                CurrencyPicker(currency = currency, onCurrencyChange = { currency = it })
                OutlinedTextField(value = opening, onValueChange = { opening = it }, label = { Text("Opening balance") }, singleLine = true)
                OutlinedTextField(
                    value = trackerId,
                    onValueChange = { trackerId = it },
                    label = { Text("Tracker ID (optional)") },
                    supportingText = { Text("ID used by the betting tracker JSON to match this account") },
                    singleLine = true
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(archived, { archived = it })
                    Text("Archived")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, type, currency, opening, trackerId, archived) },
                enabled = name.isNotBlank() && MoneyCodec.parseAtomic(opening.ifBlank { "0" }, currency) != null
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun IncomeSourceEditDialog(
    source: IncomeSourceEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, Boolean) -> Unit
) {
    var name by remember(source?.id) { mutableStateOf(source?.name.orEmpty()) }
    var currency by remember(source?.id) { mutableStateOf(source?.currency ?: "RSD") }
    var archived by remember(source?.id) { mutableStateOf(source?.archived ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (source == null) "New income source" else "Edit income source") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                CurrencyPicker(currency = currency, onCurrencyChange = { currency = it })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(archived, { archived = it })
                    Text("Archived")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, currency, archived) },
                enabled = name.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
