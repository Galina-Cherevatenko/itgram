package docker

import fixture.docker.AbstractDockerCompose

object RabbitDockerCompose : AbstractDockerCompose(
    "rabbit_1", 5672, "docker-compose-rabbit.yml"
) {
    override val user: String
        get() = "guest"
    override val password: String
        get() = "guest"
}