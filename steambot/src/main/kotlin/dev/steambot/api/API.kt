package dev.steambot.api

import dev.steambot.util.inject

object API {

    val client: Client by inject()

    val viewport: Viewport by inject()

}