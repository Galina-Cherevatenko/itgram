package ru.itgram.biz.exceptions

import ru.itgram.common.models.MkplWorkMode

class MkplPublicationDbNotConfiguredException(val workMode: MkplWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)