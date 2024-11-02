package ru.itgram.app.spring.config

import ru.itgram.app.common.IMkplAppSettings
import ru.itgram.biz.MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplPublicationProcessor
): IMkplAppSettings