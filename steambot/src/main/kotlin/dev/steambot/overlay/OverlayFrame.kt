package dev.steambot.overlay

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import com.sun.jna.ptr.IntByReference
import dev.steambot.SteamBot
import dev.steambot.rs.RSClient
import dev.steambot.util.every
import dev.steambot.util.inject
import org.tinylog.kotlin.Logger
import java.awt.*
import java.lang.reflect.Method
import javax.swing.JComponent
import javax.swing.JDialog
import javax.swing.JFrame

class OverlayFrame : JDialog() {

    private val steamBot: SteamBot by inject()
    private val client: RSClient by inject()

    private lateinit var canvas: OverlayCanvas

    lateinit var hwnd: WinDef.HWND private set

    fun open() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        bounds = steamBot.bounds
        isUndecorated = true
        isFocusable = false
        isAlwaysOnTop = true
        isModal = true
        modalityType = ModalityType.MODELESS
        background = Color(0,0,0,0)
        this.initCanvas()
        isVisible = true
        this.initTransparency()
        this.initCycleLogic()
    }

    private fun initCanvas() {
        canvas = OverlayCanvas()
        add(canvas)
    }

    private fun initTransparency() {
        hwnd = WinDef.HWND(Native.getComponentPointer(this))
        setWindowOpaque(false)
        makeTransparent()
    }

    private fun initCycleLogic() {
        client.onCycle.subscribe {
            isAlwaysOnTop = User32.INSTANCE.GetForegroundWindow() == steamBot.hwnd
            bounds = steamBot.bounds
            canvas.repaint()
        }
    }

    private fun setWindowOpaque(flag: Boolean) {
        try {
            val awtUtilsClass = Class.forName("com.sun.awt.AWTUtilities")
            val method: Method =
                awtUtilsClass.getMethod("setWindowOpaque", Window::class.java, Boolean::class.javaPrimitiveType)
            method.invoke(null, SystemColor.window, flag)
        } catch (exp: Exception) {
        }

    }

    private fun makeTransparent() {
        var style = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE)
        style = style or WinUser.WS_EX_LAYERED or WinUser.WS_EX_TRANSPARENT
        User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, style)
    }

    inner class OverlayCanvas : JComponent() {

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            val g2d = g.create() as Graphics2D
            g2d.color = Color(0, 0, 0, 0)
            g2d.fillRect(0, 0, width, height)
            g2d.color = Color.WHITE

            g2d.color = Color.RED
            g2d.fillRect(200, 200, 300, 300)

            g2d.dispose()
        }
    }
}