package dev.steambot.natives

object Offsets {
    val dwGameState = module.offset(0x531d88)
    val dwLoginState = module.offset(0x664e10)
    val dwLocalPlayerPtr = module.offset(0x1bd7fa0)
    val dwViewportTempX = module.offset(0x531d08)
    val dwViewportTempY = module.offset(0x531d0c)
    val dwViewportScalingMultiplier = module.offset(0x1D58B98)
}