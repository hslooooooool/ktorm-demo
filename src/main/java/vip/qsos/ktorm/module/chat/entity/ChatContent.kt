package vip.qsos.ktorm.module.chat.entity

/**
 * @author : 华清松
 * 聊天消息内容
 * @param fields 内容map集合
 */
data class ChatContent(
        var fields: Map<String, Any?>? = null
)