package config
// swagger url: http://localhost:8080/swagger-ui/index.html
@Suppress("unused")
@Configuration
class SwaggerConfiguration {
    @Bean
    fun springDocConfiguration() = SpringDocConfiguration()

    @Bean
    fun springDocConfigProperties() = SpringDocConfigProperties()

    @Bean
    fun objectMapperProvider(springDocConfigProperties: SpringDocConfigProperties) =
        ObjectMapperProvider(springDocConfigProperties)
}
