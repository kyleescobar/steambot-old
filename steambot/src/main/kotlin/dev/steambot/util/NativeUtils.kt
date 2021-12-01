package dev.steambot.util

import java.lang.management.ManagementFactory

fun getParentProcessID(): Int {
    val processName = ManagementFactory.getRuntimeMXBean().name
    return processName.split("@").first().toInt()
}