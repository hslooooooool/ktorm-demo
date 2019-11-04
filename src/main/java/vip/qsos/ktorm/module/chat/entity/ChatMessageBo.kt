package vip.qsos.ktorm.module.chat.entity

import com.google.gson.Gson
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IVo
import java.time.LocalDate

/**
 * @author : 华清松
 * 聊天消息,一条聊天消息对应唯一一条时间线
 * @param sessionId 会话ID
 * @param messageId 消息ID
 * @param sequence 消息顺序
 * @param content 消息内容
 */
@ApiModel(description = "聊天消息")
data class ChatMessage(
        /**@see ChatSessionBo.sessionId*/
        @ApiModelProperty(value = "会话ID", required = true)
        var sessionId: Int = -1,
        @ApiModelProperty(value = "消息ID", required = false)
        var messageId: Int = -1,
        @ApiModelProperty(value = "消息序列", required = true)
        var sequence: Int = -1,
        @ApiModelProperty(value = "消息内容", required = true)
        var content: ChatContentBo
) : IVo<TableChatMessage> {

    override fun toTable(): TableChatMessage {
        return TableChatMessage(
                messageId,
                sessionId,
                sequence,
                contentToJson()
        )
    }

    fun contentToJson(): String {
        return Gson().toJson(content)
    }

    companion object {
        fun jsonToContent(json: String): ChatContentBo {
            return Gson().fromJson(json, ChatContentBo::class.java)
        }

        fun getVo(table: TableChatMessage?): ChatMessage? {
            return table?.let {
                ChatMessage(
                        it.sessionId,
                        it.messageId,
                        it.sequence,
                        Gson().fromJson(it.content, ChatContentBo::class.java)
                )
            }
        }
    }
}

@ApiModel(description = "聊天消息")
data class ChatMessageBo(
        @ApiModelProperty(value = "聊天消息发送人信息", required = true)
        val user: ChatUserBo,
        @ApiModelProperty(value = "聊天消息发送时间", required = true)
        val createTime: LocalDate,
        @ApiModelProperty(value = "聊天消息", required = true)
        val message: ChatMessage
)