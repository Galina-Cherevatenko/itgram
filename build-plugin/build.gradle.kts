plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "ru.otus.itgram.plugin.BuildPluginJvm"
        }
        register("build-kmp") {
            id = "build-kmp"
            implementationClass = "ru.otus.itgram.plugin.BuildPluginMultiplatform"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.binaryCompatibilityValidator)
}