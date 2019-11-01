package vip.qsos.ktorm.module.chat.entity

import vip.qsos.ktorm.module.IVo
import java.time.LocalDate

/**
 * @author : 华清松
 * 聊天群
 * @param groupId 群ID,设计上等于会话ID
 * @param name 群名称
 * @param createTime 创建时间,毫秒数
 * @param notice 群公告
 *
 * @see ChatSessionBo
 */
data class ChatGroupBo(
        /** @see ChatSessionBo.sessionId */
        val groupId: Int,
        val name: String,
        val createTime: LocalDate,
        val avatar: String = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",
        val notice: String? = null
) : IVo<TableChatGroup> {

    /**此会话最后一条消息*/
    var lastMessage: ChatMessageBo? = null

    override fun toTable(): TableChatGroup {
        return TableChatGroup(
                groupId = groupId,
                name = name,
                avatar = avatar,
                notice = notice
        )
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