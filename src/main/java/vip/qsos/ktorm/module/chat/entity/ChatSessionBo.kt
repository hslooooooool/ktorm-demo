package vip.qsos.ktorm.module.chat.entity

import vip.qsos.ktorm.module.IVo

/**
 * @author : 华清松
 * 消息会话,一个消息会话关联一个唯一的群,相当于消息订阅主题,关联的群可看做是会话的信息拓展,
 * 在单聊场景下,没有群的概念,当单聊场景下增加第三人后,方才会创建群
 * @param sessionId 会话ID
 * @param type 会话类型
 *
 * @see ChatGroupBo
 */
data class ChatSessionBo(
        var sessionId: Int = -1,
        var type: ChatType,
        var hashCode: String = ""
) : IVo<TableChatSession> {

    override fun toTable(): TableChatSession {
        return TableChatSession(
                sessionId = sessionId,
                type = type,
                hashCode = hashCode
        )
    }
}