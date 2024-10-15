package test

import fixture.BaseFunSpec
import fixture.client.RestClient
import fixture.docker.DockerCompose
import io.kotest.core.annotation.Ignored
import test.action.v1.toV1

enum class TestDebug {
    STUB, PROD, TEST
}

@Ignored
open class AccRestTestBaseFull(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})