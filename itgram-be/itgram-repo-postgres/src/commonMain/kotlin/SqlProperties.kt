package ru.itgram.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "itgram-pass",
    val database: String = "itgram_publications",
    val schema: String = "public",
    val table: String = "publications",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}