package com.project1k.money

import com.project1k.money.data.MoneyCodec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyCodecTest {
    @Test fun parsesRsd() {
        assertEquals(123456L, MoneyCodec.parseAtomic("1234.56", "RSD"))
    }

    @Test fun parsesComma() {
        assertEquals(1050L, MoneyCodec.parseAtomic("10,50", "EUR"))
    }

    @Test fun parsesBitcoin() {
        assertEquals(12_345_678L, MoneyCodec.parseAtomic("0.12345678", "BTC"))
    }

    @Test fun rejectsGarbage() {
        assertNull(MoneyCodec.parseAtomic("abc", "EUR"))
    }
}
