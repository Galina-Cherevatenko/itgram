plugins {
    id("build-jvm")
    application
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.itgram.app.rabbit.ApplicationKt")
}

dependencies {

    implementation(kotlin("stdlib"))

    implementation(libs.rabbitmq.client)
    implementation(libs.jackson.databind)
    implementation(libs.logback)
    implementation(libs.coroutines.core)

    implementation(project(":itgram-common"))
    implementation(project(":itgram-app-common"))
    implementation("ru.itgram.libs:itgram-lib-logging-logback")

    // v1 api
    implementation(project(":itgram-api-v1-jackson"))
    implementation(project(":itgram-api-v1-mappers"))

    implementation(project(":itgram-biz"))
    implementation(project(":itgram-stubs"))

    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(kotlin("test"))
}