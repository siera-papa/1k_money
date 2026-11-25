package com.project1k.money.calc

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CashCalcStore(context: Context) {
    companion object {
        val denominations = listOf(5000, 2000, 1000, 500, 200, 100, 50, 20, 10)
        private val persistentDenominations = setOf(200, 100, 50, 20, 10)
        private const val PREFS = "cash_calc_counts"
    }

    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    // 500–5000 intentionally start at zero every time the app process starts.
    // 10–200 are restored from local storage and are updated immediately.
    private val _counts = MutableStateFlow(
        denominations.associateWith { denomination ->
            if (denomination in persistentDenominations) {
                prefs.getInt(key(denomination), 0).coerceAtLeast(0)
            } else {
                0
            }
        }
    )
    val counts: StateFlow<Map<Int, Int>> = _counts.asStateFlow()

    fun setCount(denomination: Int, count: Int) {
        if (denomination !in denominations) return
        val normalized = count.coerceAtLeast(0)
        val current = _counts.value
        if (current[denomination] == normalized) return
        _counts.value = current.toMutableMap().apply { put(denomination, normalized) }
        if (denomination in persistentDenominations) {
            prefs.edit().putInt(key(denomination), normalized).apply()
        }
    }

    fun increment(denomination: Int) {
        setCount(denomination, (_counts.value[denomination] ?: 0) + 1)
    }

    fun decrement(denomination: Int) {
        setCount(denomination, ((_counts.value[denomination] ?: 0) - 1).coerceAtLeast(0))
    }

    fun totalRsd(): Long = denominations.sumOf { denomination ->
        denomination.toLong() * (_counts.value[denomination] ?: 0).toLong()
    }

    private fun key(denomination: Int) = "count_$denomination"
}
