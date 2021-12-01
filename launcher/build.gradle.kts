dependencies {
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
}

tasks {
    register<Copy>("copyDll") {
        doFirst {
            project.projectDir.resolve("src/main/resources/bin/steambot.dll")
        }
        from(rootProject.projectDir.resolve("steambot-dll/cmake-build-release/steambot.dll"))
        into(project.projectDir.resolve("src/main/resources/bin/"))
    }

    register<JavaExec>("run") {
        group = "application"
        mainClass.set("dev.steambot.launcher.Launcher")
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = rootProject.projectDir
    }

    register<Jar>("fullJar") {
        group = "build"
        archiveBaseName.set("steambot-launcher")
        archiveVersion.set("")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to "dev.steambot.launcher.Launcher")
        }
        from(configurations.compileClasspath.get().map {
            if(it.isDirectory) it
            else zipTree(it)
        })
        from(sourceSets["main"].output)
    }

    "jar" {
        finalizedBy(getByName("fullJar"))
    }

    compileKotlin {
        dependsOn(getByName("copyDll"))
        dependsOn(project(":steambot").tasks.getByName("copyJar"))
    }
}