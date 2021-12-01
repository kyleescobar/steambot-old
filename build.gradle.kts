plugins {
    kotlin("jvm") version "1.6.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "dev.steambot"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }
}