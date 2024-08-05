plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

group = "ru.otus"
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
    create("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("itgram-be").task(":check"))
    }
}