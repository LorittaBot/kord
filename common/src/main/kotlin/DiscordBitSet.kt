package dev.kord.common

import dev.kord.common.annotation.KordUnsafe
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min

private const val SAFE_LENGTH = 19
private const val WIDTH = Byte.SIZE_BITS

@Suppress("FunctionName")
public fun EmptyBitSet(): DiscordBitSet = DiscordBitSet(0)

@Serializable(with = DiscordBitSetSerializer::class)
@JvmInline
public value class DiscordBitSet(internal val data: LongArray) {

    public val isEmpty: Boolean
        get() = data.all { it == 0L }

    public val value: String
        get() {
            val buffer = ByteBuffer.allocate(data.size * Long.SIZE_BYTES)
            buffer.asLongBuffer().put(data.reversedArray())
            return BigInteger(buffer.array()).toString()
        }

    public val size: Int
        get() = data.size * WIDTH

    public val binary: String
        get() = data.joinToString("") { it.toULong().toString(2) }.reversed().padEnd(8, '0')

    // TODO: Change this to "equals" when Discord adds support for equals for value classes
    public fun isEqual(other: DiscordBitSet): Boolean {
        for (i in 0 until max(data.size, other.data.size)) {
            if (getOrZero(i) != getOrZero(i)) return false
        }
        return true
    }

    private fun getOrZero(i: Int) = data.getOrNull(i) ?: 0L

    public operator fun get(index: Int): Boolean {
        if (index !in 0 until size) return false
        val indexOfWidth = index / WIDTH
        val bitIndex = index % WIDTH
        return data[indexOfWidth] and (1L shl bitIndex) != 0L
    }

    public operator fun contains(other: DiscordBitSet): Boolean {
        if (other.size > size) return false
        for (i in other.data.indices) {
            if (data[i] and other.data[i] != other.data[i]) return false
        }
        return true
    }

    /* public operator fun set(index: Int, value: Boolean) {
        if (index !in 0 until size) data.copyOf((63 + index) / WIDTH)
        val indexOfWidth = index / WIDTH
        val bitIndex = index % WIDTH
        val bit = if (value) 1L else 0L
        data[index] = data[indexOfWidth] or (bit shl bitIndex)
    } */

    @OptIn(KordUnsafe::class)
    public operator fun plus(another: DiscordBitSet): DiscordBitSet {
        val dist = data.copyOf(max(another.data.size, this.data.size))
        val copy = DiscordBitSet(dist)
        copy.unsafeAdd(another)
        return copy
    }

    @OptIn(KordUnsafe::class)
    public operator fun minus(another: DiscordBitSet): DiscordBitSet {
        val dist = LongArray(data.size)
        data.copyInto(dist)
        val copy = DiscordBitSet(dist)
        copy.unsafeRemove(another)
        return copy
    }

    /**
     * This is unsafe because the underlying LongArray is modified!
     */
    @KordUnsafe
    public fun unsafeAdd(another: DiscordBitSet) {
        if (another.data.size > data.size) error("Incorrect data size! ${another.data.size} (another) is larger than ${data.size}")
        for (i in another.data.indices) {
            data[i] = data[i] or another.data[i]
        }
    }

    /**
     * This is unsafe because the underlying LongArray is modified!
     */
    @KordUnsafe
    public fun unsafeRemove(another: DiscordBitSet) {
        for (i in 0 until min(data.size, another.data.size)) {
            data[i] = data[i] xor (data[i] and another.data[i])
        }
    }

    // TODO: Change this to "hashCode" when Discord adds support for equals for value classes
    public fun getHashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + size
        return result
    }

    override fun toString(): String {
        return "DiscordBitSet($binary)"
    }
}

public fun DiscordBitSet(vararg widths: Long): DiscordBitSet {
    return DiscordBitSet(widths)
}

public fun DiscordBitSet(value: String): DiscordBitSet {
    if (value.length <= SAFE_LENGTH) {// fast path
        return DiscordBitSet(longArrayOf(value.toULong().toLong()))
    }

    val bytes = BigInteger(value).toByteArray()

    val longSize = (bytes.size / Long.SIZE_BYTES) + 1
    val destination = LongArray(longSize)

    var longIndex = -1
    bytes.reversed().forEachIndexed { index, byte ->
        val offset = index % Long.SIZE_BYTES
        if (offset == 0) {
            longIndex += 1
        }

        destination[longIndex] =
            (destination[longIndex].toULong() or (byte.toUByte().toULong() shl offset * Byte.SIZE_BITS)).toLong()
    }

    return DiscordBitSet(destination)
}


public object DiscordBitSetSerializer : KSerializer<DiscordBitSet> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DiscordBitSet", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DiscordBitSet {
        return DiscordBitSet(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: DiscordBitSet) {
        encoder.encodeString(value.value)
    }
}
