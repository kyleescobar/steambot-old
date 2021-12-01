package dev.steambot.util.ext

import org.jire.arrowhead.Process
import org.jire.arrowhead.get

fun Process.pointer(address: Long, offset: Long = 0L): Long {
    return this[address + offset]
}