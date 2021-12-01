package dev.steambot.natives

import dev.steambot.util.retry
import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import org.jire.arrowhead.processByID
import org.tinylog.kotlin.Logger

lateinit var process: Process private set
lateinit var module: Module private set

fun attachProcess(processId: Int) {
    Logger.info("Attaching to processID: $processId")

    retry(128L) {
        process = processByID(processId)!!
    }

    retry(128L) {
        process.loadModules()
        module = process.modules["osclient.exe"]!!
    }

    Logger.info("Successfully attached to process. [Base Address: 0x${module.address.toString(16)}]")
}