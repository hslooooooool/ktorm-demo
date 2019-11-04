package vip.qsos.ktorm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "stream.http.multipart")
data class CoreProperties(
        /**文件保存路径*/
        var filePath: String = ""
)