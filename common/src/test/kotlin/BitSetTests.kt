@file:OptIn(KordUnsafe::class)

import dev.kord.common.DiscordBitSet
import dev.kord.common.annotation.KordUnsafe
import org.junit.jupiter.api.Test

class BitSetTests {
    @Test
    fun `b contains a`() {
        val a = DiscordBitSet(0b101)
        val b = DiscordBitSet(0b111)
        assert(a in b)
    }

    @Test
    fun `a equals b`() {
        val a = DiscordBitSet(0b111, 0)
        val b = DiscordBitSet(0b111)
        assert(a.isEqual(b))
    }


    @Test
    fun `get a bit`() {
        val a = DiscordBitSet(0b101, 0)
        assert(!a[1])
    }

    @Test
    fun `get a bit out of range`() {
        val a = DiscordBitSet(0b101, 0)
        assert(!a[10000])
    }

    @Test
    fun `add and remove a  bit`() {
        val a = DiscordBitSet(0b101, 0)
        a.unsafeAdd(DiscordBitSet(0b111))
        assert(a.value == 0b111.toString())
        a.unsafeRemove(DiscordBitSet(0b001))
        assert(a.value == 0b110.toString())

    }

    @Test
    fun `remove a bit`() {
        val a = DiscordBitSet(0b101, 0)
        a.unsafeRemove(DiscordBitSet(0b111))
        assert(a.value == "0")

    }

}