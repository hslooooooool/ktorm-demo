package vip.qsos.ktorm.module.chat.entity

import com.google.gson.Gson
import org.springframework.web.bind.annotation.RequestBody

/**
 * @author : 华清松
 * 聊天消息,一条聊天消息对应唯一一条时间线
 * @param sessionId 会话ID
 * @param messageId 消息ID
 * @param sequence 消息顺序
 * @param content 消息内容
 */
data class ChatMessage(
        /**@see ChatSession.sessionId*/
        var sessionId: Int = -1,
        var messageId: Int = -1,
        var sequence: Int = -1,
        var content: ChatContent
) {

    fun contentToJson(): String {
        return Gson().toJson(content)
    }

    companion object {
        fun jsonToContent(json: String): ChatContent {
            return Gson().fromJson(json, ChatContent::class.java)
        }
    }
}

data class MChatMessage(
        val user: ChatUser,
        val createTime: Long,
        val message: ChatMessage
)