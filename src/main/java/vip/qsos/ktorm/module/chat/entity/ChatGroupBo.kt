package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo
import vip.qsos.ktorm.util.DateUtils
import java.time.LocalDateTime

/**
 * @author : 华清松
 * 聊天群
 */
@ApiModel(description = "聊天群")
class ChatGroupBo : IBo<TableChatGroup> {
    /** @see ChatSessionBo.sessionId */
    @ApiModelProperty(name = "groupId", value = "聊天群ID，同sessionId")
    var groupId: Int = -1
    @ApiModelProperty(name = "name", value = "群名称")
    var name: String = ""
    @ApiModelProperty(name = "createTime", value = "创建时间")
    var createTime: String = ""
    @ApiModelProperty(name = "avatar", value = "群封面")
    var avatar: String? = null
    @ApiModelProperty(name = "notice", value = "群公告")
    var notice: String? = null
    @ApiModelProperty(name = "lastMessageId", value = "最后一条消息ID")
    var lastMessage: ChatMessageInfoBo? = null

    constructor()
    constructor(
            groupId: Int = -1, name: String, createTime: LocalDateTime = LocalDateTime.now(),
            avatar: String? = null, notice: String? = null, lastMessage: ChatMessageInfoBo? = null
    ) {
        this.groupId = groupId
        this.name = name
        this.createTime = DateUtils.format(createTime)
        this.avatar = avatar
        this.notice = notice
        this.lastMessage = lastMessage
    }

    override fun toTable(): TableChatGroup {
        return TableChatGroup(
                groupId = groupId,
                name = name,
                avatar = avatar,
                notice = notice
        )
    }

    override fun getBo(table: TableChatGroup?): IBo<TableChatGroup>? {
        return table?.let {
            ChatGroupBo(
                    groupId = table.groupId,
                    name = table.name,
                    createTime = table.gmtCreate,
                    avatar = table.avatar,
                    notice = table.notice
            )
        }
    }

    companion object {
        fun getVo(table: TableChatGroup): ChatGroupBo {
            return ChatGroupBo(
                    groupId = table.groupId,
                    name = table.name,
                    createTime = table.gmtCreate,
                    avatar = table.avatar,
                    notice = table.notice
            )
        }
    }
}