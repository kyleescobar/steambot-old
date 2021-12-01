package dev.steambot.rs

import com.sun.jna.ptr.IntByReference
import dev.steambot.api.Viewport
import dev.steambot.natives.Functions
import dev.steambot.natives.Offsets.dwViewportScalingMultiplier
import dev.steambot.natives.Offsets.dwViewportTempX
import dev.steambot.natives.Offsets.dwViewportTempY
import dev.steambot.natives.process
import dev.steambot.util.ext.pointer
import dev.steambot.util.invoke
import java.awt.Point

class RSViewport : Viewport {
    override val tempX: Int by process(dwViewportTempX)
    override val tempY: Int by process(dwViewportTempY)
    override val scalingMultiplier: Float get() = process.float(process.pointer(dwViewportScalingMultiplier, 0x8, 0x138, 0x60, 0x18, 0x80, 0x68).address)

    override fun worldToScreen(x: Int, y: Int, height: Int): Point {
        val p1 = IntByReference()
        val p2 = IntByReference()
        Functions.INSTANCE.worldToScreen(p1, p2, x, y, height)
        return Point(p1.value, p2.value)
    }
}