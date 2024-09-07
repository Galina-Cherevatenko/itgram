package controllers

import apiV1Mapper
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import config.MkplAppSettings
import controllerHelper
import ru.itgram.api.v1.models.IRequest
import ru.itgram.common.MkplContext
import ru.itgram.common.helpers.asMkplError
import ru.itgram.common.models.MkplState
import src.main.kotlin.fromTransport
import src.main.kotlin.toTransportPublication

class RabbitDirectController(
    private val appSettings: MkplAppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfig,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportPublication()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectController::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = MkplContext()
        e.printStackTrace()
        context.state = MkplState.FAILING
        context.errors.add(e.asMkplError())
        val response = context.toTransportPublication()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
