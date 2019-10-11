package vip.qsos.ktorm.module.chat.entity

import com.google.gson.Gson

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
        val sessionId: Long = -1L,
        var messageId: Long = -1L,
        var sequence: Long = -1L,
        var content: ChatContent = ChatContent()
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