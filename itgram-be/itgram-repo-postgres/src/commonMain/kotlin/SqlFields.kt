package ru.itgram.backend.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val PUBLICATION_CATEGORY = "publication_category"
    const val VISIBILITY = "visibility"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"
    const val PRODUCT_ID = "product_id"

    const val PUBLICATION_CATEGORY_TYPE = "publication_category_type"
    const val PUBLICATION_CATEGORY_POST = "post"
    const val PUBLICATION_CATEGORY_START = "start"
    const val PUBLICATION_CATEGORY_AD = "ad"

    const val VISIBILITY_TYPE = "publication_visibilities_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_GROUP = "group"

    const val FILTER_TITLE = TITLE
    const val FILTER_OWNER_ID = OWNER_ID
    const val FILTER_PUBLICATION_CATEGORY = PUBLICATION_CATEGORY

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, PUBLICATION_CATEGORY, VISIBILITY, LOCK, OWNER_ID, PRODUCT_ID,
    )
}