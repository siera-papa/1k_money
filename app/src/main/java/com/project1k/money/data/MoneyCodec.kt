package com.project1k.money.data

import java.math.BigDecimal
import java.math.RoundingMode

object MoneyCodec {
    private val scales = mapOf(
        "BTC" to 8,
        "EUR" to 2,
        "USD" to 2,
        "CNY" to 2,
        "RSD" to 2
    )

    fun scale(currency: String): Int = scales[currency.uppercase()] ?: 2

    fun parseAtomic(text: String, currency: String): Long? {
        val normalized = text.trim().replace(',', '.')
        if (normalized.isEmpty()) return null
        return runCatching {
            BigDecimal(normalized)
                .setScale(scale(currency), RoundingMode.HALF_UP)
                .movePointRight(scale(currency))
                .longValueExact()
        }.getOrNull()
    }

    fun formatAtomic(value: Long, currency: String): String {
        val s = scale(currency)
        return BigDecimal.valueOf(value, s).stripTrailingZeros().toPlainString()
    }

    fun formatWithCurrency(value: Long, currency: String): String =
        "${formatAtomic(value, currency)} ${currency.uppercase()}"
}
