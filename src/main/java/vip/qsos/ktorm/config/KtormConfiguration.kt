package vip.qsos.ktorm.config

import com.fasterxml.jackson.databind.Module
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.jackson.KtormModule
import me.liuwj.ktorm.logging.Slf4jLoggerAdapter
import me.liuwj.ktorm.support.mysql.MySqlDialect
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : Ktorm 数据库配置
 */
@Configuration
open class KtormConfiguration {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean
    open fun database(): Database {
        val logger = LoggerFactory.getLogger(Database::class.java)
        return Database.connectWithSpringSupport(dataSource, MySqlDialect, Slf4jLoggerAdapter(logger))
    }

    @Bean
    open fun ktormModule(): Module {
        return KtormModule()
    }

}
