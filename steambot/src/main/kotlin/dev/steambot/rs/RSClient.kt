package dev.steambot.rs

import dev.steambot.api.Client
import dev.steambot.natives.Offsets.dwGameState
import dev.steambot.natives.Offsets.dwLoginState
import dev.steambot.natives.process
import dev.steambot.util.invoke

class RSClient : Client {

    override var gameState: Int by process(dwGameState)

    override var loginState: Int by process(dwLoginState)

}