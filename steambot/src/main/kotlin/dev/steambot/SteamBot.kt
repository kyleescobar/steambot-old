package dev.steambot

import dev.steambot.natives.attachProcess
import dev.steambot.util.get
import dev.steambot.util.getParentProcessID
import org.koin.core.context.startKoin
import org.tinylog.kotlin.Logger
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SteamBot {

    var processId: Int = -1
        private set

    fun start() {
        Logger.info("Starting SteamBot JVM client.")

        attachProcess(this.processId)

        Test.run()
    }

    fun stop() {
        Logger.info("Stopping SteamBot JVM client.")
        exitProcess(0)
    }

    companion object {

        @JvmStatic
        fun init() {
            startKoin { modules(listOf(
                STEAMBOT_MODULE
            )) }

            val steamBot = get<SteamBot>()
            steamBot.processId = getParentProcessID()

            thread {
                try {
                    steamBot.start()
                } catch (e : Exception) {
                    Logger.error(e) { "An error occurred in the SteamBot JVM."}
                }
            }
        }
    }
}