package vip.qsos.ktorm.module.chat.entity

import com.google.gson.Gson
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo

@ApiModel(description = "聊天消息")
class ChatMessageBo : IBo<TableChatMessage> {
    @ApiModelProperty(value = "消息ID")
    var messageId: Int = -1
    /**@see ChatSessionBo.sessionId*/
    @ApiModelProperty(value = "会话ID", required = true)
    var sessionId: Int = -1
    @ApiModelProperty(value = "消息时序")
    var timeline: Int = -1
    @ApiModelProperty(value = "消息内容", required = true)
    var content: ChatContentBo = ChatContentBo()
    @ApiModelProperty(value = "是否撤回", required = true)
    var cancelBack: Boolean = false

    constructor()
    constructor(
            sessionId: Int = -1, messageId: Int = -1,
            timeline: Int = -1, cancelBack: Boolean = false, content: ChatContentBo
    ) {
        this.sessionId = sessionId
        this.messageId = messageId
        this.timeline = timeline
        this.cancelBack = cancelBack
        this.content = content
    }

    override fun toTable(): TableChatMessage {
        return TableChatMessage(
                messageId,
                sessionId,
                timeline,
                cancelBack,
                contentToJson()
        )
    }

    override fun getBo(table: TableChatMessage?): IBo<TableChatMessage>? {
        return table?.let {
            ChatMessageBo(
                    table.sessionId,
                    table.messageId,
                    table.timeline,
                    table.cancelBack,
                    jsonToContent(table.content)
            )
        }
    }

    private fun contentToJson(): String {
        return Gson().toJson(content)
    }

    private fun jsonToContent(json: String): ChatContentBo {
        return Gson().fromJson(json, ChatContentBo::class.java)
    }

}

@ApiModel(description = "聊天消息详细数据")
data class ChatMessageInfoBo(
        @ApiModelProperty(value = "聊天消息发送人信息", required = true)
        val user: ChatUserBo,
        @ApiModelProperty(value = "聊天消息发送时间", required = true)
        val createTime: String,
        @ApiModelProperty(value = "聊天消息", required = true)
        val message: ChatMessageBo,
        @ApiModelProperty(value = "聊天消息读取数")
        var readNum: Int = 1,
        @ApiModelProperty(value = "聊天消息当前用户读取状态")
        var readStatus: Boolean = true
)