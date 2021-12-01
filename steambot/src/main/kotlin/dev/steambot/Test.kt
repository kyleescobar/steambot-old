package dev.steambot

import dev.steambot.api.Client
import dev.steambot.util.every
import dev.steambot.util.inject

object Test {

    private val client: Client by inject()

    fun run() {
        every(1L) {
            if(client.loginState == 26 || client.loginState == 0) {
                client.loginState = 12
            }
        }
    }
}