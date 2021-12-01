package dev.steambot

import dev.steambot.api.Client
import dev.steambot.rs.RSClient
import org.koin.dsl.bind
import org.koin.dsl.module

val STEAMBOT_MODULE = module {
    single { SteamBot() }
    single { RSClient() } bind(Client::class)
}