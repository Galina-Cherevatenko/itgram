plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.itgram"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}

tasks {
    val buildImages: Task by creating {
        dependsOn(gradle.includedBuild("itgram-be").task(":buildImages"))
    }
    val e2eTests: Task by creating {
        dependsOn(gradle.includedBuild("itgram-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    create("check") {
        group = "verification"
        dependsOn(buildImages)
        dependsOn(e2eTests)
    }
}