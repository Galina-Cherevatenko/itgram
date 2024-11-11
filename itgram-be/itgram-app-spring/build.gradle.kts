plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":itgram-common"))
    implementation(project(":itgram-app-common"))

    // v1 api
    implementation(project(":itgram-api-v1-jackson"))
    implementation(project(":itgram-api-v1-mappers"))

    // biz
    implementation(project(":itgram-biz"))

    // db
    implementation(projects.itgramRepoStubs)
    implementation(projects.itgramRepoInmemory)
    implementation(projects.itgramRepoPostgres)
    testImplementation(projects.itgramRepoCommon)
    testImplementation(projects.itgramStubs)

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.spring.mockk)

    // stubs
    testImplementation(project(":itgram-stubs"))
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    environment("MKPLADS_DB", "test_db")
}
