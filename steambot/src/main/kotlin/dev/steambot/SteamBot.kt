package dev.steambot

import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import dev.steambot.natives.attachProcess
import dev.steambot.overlay.OverlayManager
import dev.steambot.rs.RSClient
import dev.steambot.util.every
import dev.steambot.util.get
import dev.steambot.util.getParentProcessID
import dev.steambot.util.inject
import org.koin.core.context.startKoin
import org.tinylog.kotlin.Logger
import java.awt.Rectangle
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SteamBot {

    private val overlayManager: OverlayManager by inject()
    private val client: RSClient by inject()

    var processId: Int = -1
        private set

    lateinit var hwnd: WinDef.HWND private set

    fun start() {
        Logger.info("Starting SteamBot JVM client.")

        hwnd = WindowUtils.getAllWindows(true).first { it.title == "Old School RuneScape" }.hwnd
        attachProcess(this.processId)
        overlayManager.init()
        this.startThreads()
        Test.run()
    }

    fun stop() {
        Logger.info("Stopping SteamBot JVM client.")
        exitProcess(0)
    }

    val bounds: Rectangle get() {
        val info = WinUser.WINDOWINFO()
        User32.INSTANCE.GetWindowInfo(hwnd, info)

        return Rectangle(
            info.rcClient.left,
            info.rcClient.top,
            info.rcClient.right - info.rcClient.left,
            info.rcClient.bottom - info.rcClient.top
        )
    }

    private fun startThreads() {
        every(0L) {
            client.onCycle.onNext(client)
        }
    }

    companion object {

        private val DI_MODULES = listOf(
            STEAMBOT_MODULE
        )

        @JvmStatic
        fun init() {
            System.setProperty("sun.java2d.noddraw", "true")

            startKoin { modules(DI_MODULES) }

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