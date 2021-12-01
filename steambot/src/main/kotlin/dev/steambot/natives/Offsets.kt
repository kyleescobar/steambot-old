package dev.steambot.natives

object Offsets {
    val dwGameState = module.offset(0x531d88)
    val dwLoginState = module.offset(0x664e10)
    val dwLocalPlayerPtr = module.offset(0x1bd7fa0)
}