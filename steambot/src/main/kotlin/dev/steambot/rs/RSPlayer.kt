package dev.steambot.rs

import dev.steambot.api.Player
import dev.steambot.natives.process
import dev.steambot.util.invoke

class RSPlayer(private val address: Long = 0L) : Player {

    override val x: Int by process(address + 0x10)

    override val y: Int by process(address + 0x14)

    override val rotation: Int by process(address + 0x18)
}