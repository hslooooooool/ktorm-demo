package vip.qsos.ktorm.config

import com.google.gson.GsonBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.DateFormatter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import vip.qsos.ktorm.interseptor.AuthorizationInterceptor
import java.util.*

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : WebMvc 配置类
 */
@Configuration
open class WebConfig(private val authorizationInterceptor: AuthorizationInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        // 配置请求权限校验与需要校验的请求路径
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("**")
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val gsonHttpMessageConverter = GsonHttpMessageConverter()
        val jsonConfig = GsonBuilder()
        jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss")
        converters.add(gsonHttpMessageConverter)
        super.configureMessageConverters(converters)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldType(Date::class.java, DateFormatter("yyyy-MM-dd HH:mm:ss"))
    }
}
