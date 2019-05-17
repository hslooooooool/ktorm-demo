package vip.qsos.ktorm.datasources

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
@Configuration
open class DynamicDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    open fun dataSource(): DataSource {
        return DruidDataSourceBuilder.create().build();
    }

}
