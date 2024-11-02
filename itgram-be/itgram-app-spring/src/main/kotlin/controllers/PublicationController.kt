package ru.itgram.app.spring.controllers

import org.springframework.web.bind.annotation.*
import ru.itgram.api.v1.models.*
import ru.itgram.app.common.controllerHelper
import ru.itgram.app.spring.config.MkplAppSettings
import src.main.kotlin.fromTransport
import src.main.kotlin.toTransportPublication
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v1/publication")
class PublicationController(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: PublicationCreateRequest): PublicationCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: PublicationReadRequest): PublicationReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: PublicationUpdateRequest): PublicationUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: PublicationDeleteRequest): PublicationDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: PublicationSearchRequest): PublicationSearchResponse =
        process(appSettings, request = request, this::class, "search")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: MkplAppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportPublication() as R },
            clazz,
            logId,
        )
    }
}
