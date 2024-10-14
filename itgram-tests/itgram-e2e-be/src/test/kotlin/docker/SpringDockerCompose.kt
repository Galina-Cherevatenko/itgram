package docker

import fixture.docker.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "docker-compose-spring-pg.yml"
)