package ru.itgram.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.itgram.backend.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class PublicationConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "itgram-pass",
    var database: String = "itgram_publications",
    var schema: String = "public",
    var table: String = "publicationss",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
