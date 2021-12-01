package dev.steambot.api

import java.awt.Point

interface Viewport {

    val tempX: Int

    val tempY: Int

    val scalingMultiplier: Float

    fun worldToScreen(x: Int, y: Int, height: Int): Point

}