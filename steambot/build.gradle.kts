dependencies {
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
    implementation("io.insert-koin:koin-core:_")
    implementation("org.jire.arrowhead:arrowhead:_")
    implementation("it.unimi.dsi:fastutil:_")
    implementation("io.reactivex.rxjava3:rxjava:_")
    implementation("io.reactivex.rxjava3:rxkotlin:_")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
}

tasks {
    register<Jar>("fullJar") {
        group = "build"
        archiveBaseName.set("steambot")
        archiveClassifier.set("")
        archiveVersion.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.compileClasspath.get().map {
            if(it.isDirectory) it
            else zipTree(it)
        })
        from(sourceSets["main"].output)
    }

    register<Copy>("copyJar") {
        doFirst {
            project(":launcher").projectDir.resolve("src/main/resources/bin/steambot.jar").deleteRecursively()
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(getByName("fullJar"))
        into(project(":launcher").projectDir.resolve("src/main/resources/bin/"))
    }
}