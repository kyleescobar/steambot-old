package dev.steambot.api

import io.reactivex.rxjava3.subjects.PublishSubject

interface Client {

    var gameState: Int

    var loginState: Int

    val localPlayer: Player

    val onCycle: PublishSubject<Client>

}