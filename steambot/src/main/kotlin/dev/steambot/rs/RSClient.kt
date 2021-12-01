package dev.steambot.rs

import dev.steambot.api.Client
import dev.steambot.natives.Offsets.dwGameState
import dev.steambot.natives.Offsets.dwLoginState
import dev.steambot.natives.process
import dev.steambot.util.invoke
import io.reactivex.rxjava3.subjects.PublishSubject

class RSClient : Client {

    override var gameState: Int by process(dwGameState)

    override var loginState: Int by process(dwLoginState)

    /**
     * Callback Observables
     */
    override val onCycle = PublishSubject.create<Client>()
}