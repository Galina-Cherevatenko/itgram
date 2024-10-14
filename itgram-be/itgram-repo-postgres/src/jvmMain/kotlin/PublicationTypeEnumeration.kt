package ru.itgram.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import ru.itgram.backend.repo.postgresql.SqlFields
import ru.itgram.common.models.MkplPublicationCategory

fun Table.publicationCategoryEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.PUBLICATION_CATEGORY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.PUBLICATION_CATEGORY_START -> MkplPublicationCategory.START
            SqlFields.PUBLICATION_CATEGORY_AD -> MkplPublicationCategory.AD
            else -> MkplPublicationCategory.POST
        }
    },
    toDb = { value ->
        when (value) {
            MkplPublicationCategory.START -> PgPublicationCategoryStart
            MkplPublicationCategory.AD -> PgPublicationCategoryAd
            MkplPublicationCategory.POST -> PgPublicationCategoryPost
        }
    }
)

sealed class PgPublicationCategoryValue(enVal: String): PGobject() {
    init {
        type = SqlFields.PUBLICATION_CATEGORY_TYPE
        value = enVal
    }
}

object PgPublicationCategoryPost: PgPublicationCategoryValue(SqlFields.PUBLICATION_CATEGORY_POST)
object PgPublicationCategoryAd: PgPublicationCategoryValue(SqlFields.PUBLICATION_CATEGORY_AD)
object PgPublicationCategoryStart: PgPublicationCategoryValue(SqlFields.PUBLICATION_CATEGORY_START)
