import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import config.MkplAppSettings
import config.RabbitConfig
import config.RabbitExchangeConfiguration
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.itgram.api.v1.models.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
            RabbitMQContainer("rabbitmq:latest").apply {
                withExposedPorts(5672, 15672)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = MkplAppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
        controllersConfig = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = exchange,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun publicationCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfig) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, PublicationCreateResponse::class.java)
                val expected = MkplPublicationStub.get()

                assertEquals(expected.title, response.publication?.title)
                assertEquals(expected.description, response.publication?.description)
            }
        }
    }

    private val boltCreateV1 = with(MkplPublicationStub.get()) {
        PublicationCreateRequest(
            publication = PublicationCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = PublicationDebug(
                mode = PublicationRequestDebugMode.STUB,
                stub = PublicationRequestDebugStubs.SUCCESS
            )
        )
    }
}
