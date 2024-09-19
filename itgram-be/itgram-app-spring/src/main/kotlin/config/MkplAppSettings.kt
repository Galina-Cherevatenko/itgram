package ru.itgram.app.spring.config

import IMkplAppSettings
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplPublicationProcessor
): IMkplAppSettings