package config

@Suppress("unused")
@Configuration
class SerialiazationConfiguration {
    @Bean
    fun messageConverter(): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(apiV2Mapper)
    }
}