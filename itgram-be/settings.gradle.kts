rootProject.name = "itgram-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":itgram-api-v1-jackson")
include(":itgram-api-v1-mappers")
include(":itgram-api-log")

include(":itgram-common")
include(":itgram-biz")
include(":itgram-stubs")

include(":itgram-app-common")
include(":itgram-app-spring")
include(":itgram-app-rabbit")

include(":itgram-repo-common")
include(":itgram-repo-inmemory")
include(":itgram-repo-stubs")
include(":itgram-repo-tests")