package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo

/**
 * @author : 华清松
 * 消息会话,一个消息会话关联一个唯一的群,相当于消息订阅主题,关联的群可看做是会话的信息拓展,
 * 在单聊场景下,没有群的概念,当单聊场景下增加第三人后,方才会创建群
 *
 * @see ChatGroupBo
 */
@ApiModel(description = "消息会话")
class ChatSessionBo : IBo<TableChatSession> {
    @ApiModelProperty(name = "sessionId", value = "会话ID")
    var sessionId: Int = -1
    @ApiModelProperty(name = "type", value = "会话类型")
    var type: ChatSessionType = ChatSessionType.SINGLE
    @ApiModelProperty(name = "hashCode", value = "会话唯一判定值")
    var hashCode: String = ""

    constructor()
    constructor(
            sessionId: Int = -1,
            type: ChatSessionType,
            hashCode: String
    ) {
        this.sessionId = sessionId
        this.type = type
        this.hashCode = hashCode
    }

    override fun toTable(): TableChatSession {
        return TableChatSession(
                sessionId = sessionId,
                type = type,
                hashCode = hashCode
        )
    }

    override fun getBo(table: TableChatSession?): IBo<TableChatSession>? {
        return table?.let {
            ChatSessionBo(
                    sessionId = table.sessionId,
                    type = table.type,
                    hashCode = table.hashCode
            )
        }
    }
}