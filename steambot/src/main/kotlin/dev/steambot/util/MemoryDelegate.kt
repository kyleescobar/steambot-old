package dev.steambot.util

import org.jire.arrowhead.Process
import org.jire.arrowhead.get
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline operator fun <reified T : Any> Process.invoke(address: Long, offset: Long = 0L) = object : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return this@invoke.get(address + offset) as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = when(T::class) {
        Byte::class -> this@invoke.set(address + offset, value as Byte)
        Short::class -> this@invoke.set(address + offset, value as Short)
        Int::class -> this@invoke.set(address + offset, value as Int)
        Double::class -> this@invoke.set(address + offset, value as Double)
        Float::class -> this@invoke.set(address + offset, value as Float)
        Long::class -> this@invoke.set(address + offset, value as Long)
        Char::class -> this@invoke.set(address + offset, value as Char)
        Boolean::class -> this@invoke.set(address + offset, value as Boolean)
        else -> throw IllegalArgumentException()
    }
}