package dev.steambot.launcher

import org.tinylog.kotlin.Logger
import java.io.File
import java.io.FileOutputStream
import java.util.zip.CRC32
import kotlin.system.exitProcess

object Updater {

    private val dir = Launcher.STEAMBOT_DIR.resolve("bin/")

    fun run() {
        Logger.info("Checking for SteamBot updates...")

        update("steambot.jar")
        update("steambot.dll")

        Logger.info("SteamBot is up-to-date.")
    }

    private fun update(fileName: String) {
        val latestBytes = fileName.getResourceBytes()

        val file = dir.resolve(fileName)

        if(!file.exists()) {
            file.updateBytes(latestBytes)
        }

        val currentBytes = file.inputStream().readAllBytes()

        val currentCrc = currentBytes.crc()
        val latestCrc = latestBytes.crc()

        if(currentCrc != latestCrc) {
            Logger.info("Updating SteamBot file: ${file.path}.")

            file.deleteRecursively()

            FileOutputStream(file).use {
                it.write(latestBytes)
            }
        }
    }

    private fun File.updateBytes(bytes: ByteArray) {
        Logger.info("Updating file: ${this.name}...")

        this.deleteRecursively()
        FileOutputStream(File(this.absolutePath)).use {
            it.write(bytes)
        }
    }

    private fun String.getResourceBytes(): ByteArray = try {
        Updater::class.java.getResourceAsStream("/bin/$this")?.readAllBytes()
            ?: throw IllegalArgumentException("Failed to read resource bytes from: /bin/$this")
    } catch(e : Exception) {
        Logger.error(e) { "An error occurred while reading embedded file data." }
        exitProcess(1)
    }

    private fun ByteArray.crc(): Long {
        val crc = CRC32()
        crc.update(this, 0, this.size)
        return crc.value
    }
}