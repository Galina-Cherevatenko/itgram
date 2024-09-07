package controllers

import com.rabbitmq.client.*
import config.RabbitConfig
import config.RabbitExchangeConfiguration
import kotlinx.coroutines.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import ru.itgram.logging.common.MpLoggerProvider
import kotlin.coroutines.CoroutineContext

abstract class RabbitProcessorBase @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val rabbitConfig: RabbitConfig,
    override val exchangeConfig: RabbitExchangeConfiguration,
    val loggerProvider: MpLoggerProvider,
    private val dispatcher: CoroutineContext = Dispatchers.IO.limitedParallelism(1) + Job(),
) : IRabbitMqController {
    private val logger = loggerProvider.logger(this::class)
    private var conn: Connection? = null
    private var chan: Channel? = null
    override suspend fun process() = withContext(dispatcher) {
        ConnectionFactory().apply {
            host = rabbitConfig.host
            port = rabbitConfig.port
            username = rabbitConfig.user
            password = rabbitConfig.password
        }.newConnection().use { connection ->
            logger.debug("Creating new connection [${exchangeConfig.consumerTag}]")
            conn = connection
            connection.createChannel().use { channel ->
                logger.debug("Creating new channel [${exchangeConfig.consumerTag}]")
                chan = channel
                val deliveryCallback = channel.getDeliveryCallback()
                val cancelCallback = getCancelCallback()
                channel.describeAndListen(deliveryCallback, cancelCallback)
            }
        }
    }

    /**
     * Обработка поступившего сообщения в deliverCallback
     */
    protected abstract suspend fun Channel.processMessage(message: Delivery)

    /**
     * Обработка ошибок
     */
    protected abstract fun Channel.onError(e: Throwable, delivery: Delivery)

    /**
     * Callback, который вызывается при доставке сообщения консьюмеру
     */
    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, delivery: Delivery ->
        runBlocking {
            kotlin.runCatching {
                val deliveryTag: Long = delivery.envelope.deliveryTag
                processMessage(delivery)
                this@getDeliveryCallback.basicAck(deliveryTag, false)
            }.onFailure {
                onError(it, delivery)
            }
        }
    }

    /**
     * Callback, вызываемый при отмене консьюмера
     */
    private fun getCancelCallback() = CancelCallback {
        logger.debug("[$it] was cancelled")
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback
    ) {
        withContext(Dispatchers.IO) {
            exchangeDeclare(exchangeConfig.exchange, exchangeConfig.exchangeType)
            queueDeclare(exchangeConfig.queue, false, false, false, null)
            queueBind(exchangeConfig.queue, exchangeConfig.exchange, exchangeConfig.keyIn)
            basicConsume(
                exchangeConfig.queue,
                false,
                exchangeConfig.consumerTag,
                deliverCallback,
                cancelCallback
            )

            while (isOpen) {
                runCatching {
                    delay(100)
                }.recover {  }
            }
            logger.debug("Channel for [${exchangeConfig.consumerTag}] was closed.")
        }
    }

    override fun close() {
        logger.debug("CLOSE is requested [${exchangeConfig.consumerTag}]")
        chan?.takeIf { it.isOpen }?.run {
            basicCancel(exchangeConfig.consumerTag)
            close()
            logger.debug("Close channel [${exchangeConfig.consumerTag}]")
        }
        conn?.takeIf { it.isOpen }?.run {
            close()
            logger.debug("Close Rabbit connection [${exchangeConfig.consumerTag}]")
        }
    }
}
