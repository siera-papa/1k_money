package com.project1k.money.data

data class CurrencySpec(
    val code: String,
    val symbol: String,
    val displayName: String
)

object CurrencyCatalog {
    val all = listOf(
        CurrencySpec("RSD", "din.", "Dinar"),
        CurrencySpec("USD", "$", "Dollar"),
        CurrencySpec("CNY", "¥", "Yuan"),
        CurrencySpec("BTC", "₿", "Bitcoin"),
        CurrencySpec("EUR", "€", "Euro")
    )

    fun spec(code: String): CurrencySpec =
        all.firstOrNull { it.code.equals(code, ignoreCase = true) }
            ?: CurrencySpec(code.uppercase(), code.uppercase(), code.uppercase())

    fun symbol(code: String): String = spec(code).symbol

    /** Compact label used next to accounts/sources. Keep the symbol for amount fields only. */
    fun label(code: String): String = spec(code).code

    /** Human-readable preset label used only in the currency picker. */
    fun pickerLabel(code: String): String {
        val s = spec(code)
        return "${s.displayName} (${s.code})"
    }
}
