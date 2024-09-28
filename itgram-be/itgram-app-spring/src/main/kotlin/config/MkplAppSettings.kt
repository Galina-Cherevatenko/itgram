package ru.itgram.app.spring.config

import IMkplAppSettings
import MkplPublicationProcessor
import ru.itgram.common.MkplCorSettings

class MkplAppSettings(
    override val corSettings: MkplCorSettings,
    override val processor: MkplPublicationProcessor
): IMkplAppSettings