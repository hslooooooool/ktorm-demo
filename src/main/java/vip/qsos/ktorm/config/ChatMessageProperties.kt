package vip.qsos.ktorm.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "chat.message")
data class ChatMessageProperties(
        /**消息可撤销时长，默认5秒*/
        var cancellimit: Int = 5
)