package vip.qsos.ktorm.config

import com.google.gson.GsonBuilder
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.DateFormatter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import vip.qsos.ktorm.interseptor.AuthorizationInterceptor
import java.util.*

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : WebMvc 配置类
 */
@Configuration
open class WebConfig(
        private val authorizationInterceptor: AuthorizationInterceptor,
        private val mProperties: MultipartProperties
) : WebMvcConfigurer {

    private val pattern = "yyyy-MM-dd HH:mm:ss"

    override fun addInterceptors(registry: InterceptorRegistry) {
        // 配置请求权限校验与需要校验的请求路径
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("**")
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val gsonHttpMessageConverter = GsonHttpMessageConverter()
        val jsonConfig = GsonBuilder()
        jsonConfig.setDateFormat(pattern)
        converters.add(gsonHttpMessageConverter)
        super.configureMessageConverters(converters)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldType(Date::class.java, DateFormatter(pattern))
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        super.addResourceHandlers(registry)
        // FIXME 映射文件访问路径
        registry.addResourceHandler("/${mProperties.location}/**").addResourceLocations("file:${mProperties.location}/");
    }
}
