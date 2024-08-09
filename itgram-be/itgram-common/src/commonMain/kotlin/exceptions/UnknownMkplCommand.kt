package ru.itgram.common.exceptions

import ru.itgram.common.models.MkplCommand

class UnknownMkplCommand(command: MkplCommand) : Throwable("Wrong command $command at mapping toTransport stage")