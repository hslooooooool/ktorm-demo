package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findListByIds
import me.liuwj.ktorm.entity.findOne
import org.springframework.stereotype.Service
import vip.qsos.ktorm.module.chat.entity.*

/**
 * @author : 华清松
 * @date : 2019/11/2
 * @description : 聊天消息服务
 */
@Service
open class ChatGroupService : IChatService.IGroup {

    override fun getGroupWithMe(userId: Int): List<ChatGroupBo> {
        val groupIds = DBChatUserWithSession.findList {
            it.userId eq userId
        }.map {
            it.sessionId
        }.toSet()
        val groupList = DBChatGroup.findListByIds(groupIds).map { group ->
            val chatGroup = ChatGroupBo.getVo(group)

            val message = group.lastMessageId?.let { messageId ->
                ChatMessage.getVo(DBChatMessage.findOne {
                    it.messageId eq messageId
                })
            }
            message?.let {
                val chatUserWithMessage = DBChatUserWithMessage.findOne {
                    it.messageId eq message.messageId
                }!!
                val user = ChatUserBo.getBo(DBChatUser.findById(chatUserWithMessage.userId)!!)

                chatGroup.lastMessage = ChatMessageBo(
                        user = user,
                        createTime = chatUserWithMessage.gmtCreate,
                        message = message
                )
            }
            chatGroup
        }
        return groupList
    }

    override fun getGroupById(groupId: Int): ChatGroupBo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGroupByBySessionId(sessionId: Int): ChatGroupBo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateGroupNotice(notice: String): ChatGroupBo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateGroupName(name: String): ChatGroupBo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}