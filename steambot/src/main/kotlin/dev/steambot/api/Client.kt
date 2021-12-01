package dev.steambot.api

import io.reactivex.rxjava3.subjects.PublishSubject

interface Client {

    var gameState: Int

    var loginState: Int

    val onCycle: PublishSubject<Client>
}