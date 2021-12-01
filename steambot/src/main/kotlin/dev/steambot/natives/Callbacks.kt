package dev.steambot.natives

import org.tinylog.kotlin.Logger

object Callbacks {

    @JvmStatic
    fun doCycle() {
        Logger.info("Jvm callback on doCycle!!!")
    }

}