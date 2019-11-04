package vip.qsos.ktorm.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(CoreProperties::class)
open class CoreConfiguration : WebMvcConfigurer