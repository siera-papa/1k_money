package com.project1k.money.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project1k.money.calc.CashCalcStore

@Composable
fun CalcScreen(
    store: CashCalcStore,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    // A fresh composition starts a new counting session; totals remain in the store.
    val initialCounts = remember(store) { store.counts.value.toMap() }
    val counts by store.counts.collectAsState()
    val total = CashCalcStore.denominations.sumOf { denomination ->
        denomination.toLong() * (counts[denomination] ?: 0).toLong()
    }
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scroll)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = if (compact) 18.dp else 20.dp,
                vertical = if (compact) 12.dp else 18.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ukupno", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            Text(
                text = "${formatWhole(total)} RSD",
                fontSize = if (compact) 22.sp else 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (!compact) {
            Text(
                "10–200 ostaju sačuvani; 500–5000 se resetuju u sledećoj app sesiji.",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Apoen", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Text("Komada", modifier = Modifier.width(188.dp), fontWeight = FontWeight.SemiBold)
        }
        HorizontalDivider()

        CashCalcStore.denominations.forEach { denomination ->
            DenominationRow(
                denomination = denomination,
                count = counts[denomination] ?: 0,
                change = (counts[denomination] ?: 0) - (initialCounts[denomination] ?: 0),
                compact = compact,
                onCountChange = { store.setCount(denomination, it) },
                onMinus = { store.decrement(denomination) },
                onPlus = { store.increment(denomination) }
            )
            HorizontalDivider()
        }
        Spacer(Modifier.height(if (compact) 30.dp else 16.dp))
    }
}

@Composable
private fun DenominationRow(
    denomination: Int,
    count: Int,
    change: Int,
    compact: Boolean,
    onCountChange: (Int) -> Unit,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    var text by remember(denomination) { mutableStateOf(count.toString()) }
    var focused by remember(denomination) { mutableStateOf(false) }

    LaunchedEffect(count, focused) {
        if (!focused && text != count.toString()) text = count.toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = if (compact) 1.dp else 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(
                text = formatWhole(denomination.toLong()),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${if (change > 0) "+" else ""}$change kom.",
                style = MaterialTheme.typography.bodySmall,
                color = if (change < 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onMinus) {
            Icon(Icons.Default.Remove, contentDescription = "Minus one")
        }
        OutlinedTextField(
            value = text,
            onValueChange = { raw ->
                if (raw.length <= 6 && raw.all(Char::isDigit)) {
                    text = raw
                    raw.toIntOrNull()?.let(onCountChange)
                }
            },
            modifier = Modifier
                .width(92.dp)
                .onFocusChanged { state ->
                    focused = state.isFocused
                    if (!state.isFocused && text.isBlank()) text = count.toString()
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        IconButton(onClick = onPlus) {
            Icon(Icons.Default.Add, contentDescription = "Plus one")
        }
        Spacer(Modifier.width(4.dp))
    }
}

private fun formatWhole(value: Long): String {
    val digits = value.toString()
    return digits.reversed().chunked(3).joinToString(" ").reversed()
}
