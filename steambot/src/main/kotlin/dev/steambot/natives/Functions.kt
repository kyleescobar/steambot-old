package dev.steambot.natives

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.ptr.IntByReference

interface Functions : Library {

    fun worldToScreen(screenX: IntByReference, screenY: IntByReference, x: Int, y: Int, height: Int)

    companion object {
        val INSTANCE by lazy { Native.load("steambot", Functions::class.java) }
    }
}