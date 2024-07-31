pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "lessons"

include("m2l2-coroutines")
include("m2l3-flows")

include(":m2l6-gradle:sub1:ssub1", ":m2l6-gradle:sub1:ssub2")

include(":m2l6-gradle-sub2")
project(":m2l6-gradle-sub2").apply {
    projectDir = file("m2l6-gradle/sub2")
    name = "m2l6-custom-sub2"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")