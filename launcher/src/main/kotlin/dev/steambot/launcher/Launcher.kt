package dev.steambot.launcher

import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.ptr.IntByReference
import org.tinylog.kotlin.Logger
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import java.util.zip.ZipFile

object Launcher {

    val STEAMBOT_DIR = System.getProperty("user.home").let { File(it).resolve(".steambot/") }

    const val JRE_DOWNLOAD_URL = "https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.13%2B8/OpenJDK11U-jre_x64_windows_hotspot_11.0.13_8.zip"


    private var processId: Int = -1

    private fun launch() {
        Logger.info("Launching SteamBot client...")

        this.checkDirs()
        this.downloadAdoptJDK()
        Updater.run()
        this.startSteamClient()
        Injector.injectDLL(STEAMBOT_DIR.resolve("bin/steambot.dll"), this.processId)

        Logger.info("SteamBot client launcher has completed successfully. Exiting launcher process.")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Logger.info("Starting SteamBot client launcher...")

        this.launch()

        Logger.info("SteamBot client launcher has completed successfully.")
    }

    private fun checkDirs() {
        listOf(
            ".steambot/",
            ".steambot/bin/",
            ".steambot/java/",
        ).map { File(System.getProperty("user.home")).resolve(it) }.forEach { dir ->
            if(!dir.exists()) {
                Logger.info("Creating required directory: ${dir.path}.")
                dir.mkdirs()
            }
        }
    }

    private fun downloadAdoptJDK() {
        if(STEAMBOT_DIR.resolve("java/").listFiles()!!.isEmpty()) {
            Logger.info("Downloading AdoptJDK 11 for SteamBot...")

            val bytes = URL(JRE_DOWNLOAD_URL).openConnection().getInputStream().readAllBytes()

            FileOutputStream(STEAMBOT_DIR.resolve("java/adoptjdk11.zip")).use {
                it.write(bytes)
            }

            Logger.info("Extracting AdoptJDK 11 archive...")

            val dir = STEAMBOT_DIR.resolve("java/")

            ZipFile(STEAMBOT_DIR.resolve("java/adoptjdk11.zip")).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    if(entry.isDirectory) {
                        dir.resolve(entry.name).mkdirs()
                    } else {
                        zip.getInputStream(entry).use { input ->
                            dir.resolve(entry.name).outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }

            STEAMBOT_DIR.resolve("java/adoptjdk11.zip").deleteRecursively()

            Logger.info("Successfully downloaded and extracted AdoptJRE 11 for SteamBot.")
        }
    }

    private fun startSteamClient() {
        Logger.info("Starting Old School RuneScape from Steam...")

        val protocol = URI("steam://run/1343370")
        Desktop.getDesktop().browse(protocol)

        /*
         * Wait until the Steam client process starts and is detected running.
         */

        var found = false

        while(!found) {
            val windows = WindowUtils.getAllWindows(true)
            windows.forEach {
                if(it.title == "Old School RuneScape") {
                    val pid = IntByReference()
                    User32.INSTANCE.GetWindowThreadProcessId(it.hwnd, pid)
                    this.processId = pid.value

                    Logger.info("Found Old School RuneScape client with processID: $processId")
                    found = true
                }
            }
        }
    }
}