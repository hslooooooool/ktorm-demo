package vip.qsos.ktorm.config

import com.google.common.collect.Lists.newArrayList
import io.swagger.annotations.ApiOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    @Bean
    open fun createRestApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("QSOS")
                .select()
                // 加了 ApiOperation 注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(security())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("Ktorm ORM 演示项目接口文档")
                .description("Ktorm ORM 演示项目接口文档")
                .termsOfServiceUrl("https://github.com/vincentlauvlwj/Ktorm")
                .version("1.0.0")
                .contact(Contact("华清松", "https://github.com/hslooooooool/ktorm-demo", "821034742@qq.com"))
                .license("Apache 2.0")
                .build()
    }

    private fun security(): List<ApiKey> {
        return newArrayList(ApiKey("token", "token", "header"))
    }

}
