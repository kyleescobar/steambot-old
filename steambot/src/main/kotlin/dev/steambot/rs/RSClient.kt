package dev.steambot.rs

import dev.steambot.api.Client
import dev.steambot.natives.Offsets.dwGameState
import dev.steambot.natives.Offsets.dwLocalPlayerPtr
import dev.steambot.natives.Offsets.dwLoginState
import dev.steambot.natives.process
import dev.steambot.util.ext.pointer
import dev.steambot.util.invoke
import io.reactivex.rxjava3.subjects.PublishSubject

class RSClient : Client {
    override var gameState: Int by process(dwGameState)
    override var loginState: Int by process(dwLoginState)
    override val localPlayer: RSPlayer get() = RSPlayer(process.pointer(dwLocalPlayerPtr, 0x8).address)

    /**
     * Callback Observables
     */
    override val onCycle = PublishSubject.create<Client>()
}