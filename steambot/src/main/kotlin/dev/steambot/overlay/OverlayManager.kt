package dev.steambot.overlay

import org.tinylog.kotlin.Logger

class OverlayManager {

    val overlayFrame = OverlayFrame()

    fun init() {
        Logger.info("Initializing overlay manager.")

        overlayFrame.open()
    }

}