package dev.steambot.util.ext

import org.jire.arrowhead.Addressed
import org.jire.arrowhead.Process

fun Process.pointer(address: Long, vararg offsets: Long): Addressed {
    var currentAddress = this.long(address)
    for(i in 0 until offsets.size - 1) {
        currentAddress = this.long(currentAddress + offsets[i])
    }

    return object : Addressed {
        override val address: Long = currentAddress + offsets.last()
    }
}