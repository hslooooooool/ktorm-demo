package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findListByIds
import me.liuwj.ktorm.entity.findOne
import org.springframework.stereotype.Service
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.util.DateUtils

@Service
open class ChatGroupService : IChatService.IGroup {

    override fun getGroupWithMe(userId: Int): List<ChatGroupBo> {
        val groupIds = DBChatUserWithSession.findList {
            it.userId eq userId
        }.map {
            it.sessionId
        }.toSet()
        return DBChatGroup.findListByIds(groupIds).map { group ->
            val chatGroup = ChatGroupBo().getBo(group) as ChatGroupBo

            val message = group.lastMessageId?.let { messageId ->
                ChatMessageBo().getBo(
                        DBChatMessage.findById(messageId)?.let {
                            if (it.cancelBack) null else it
                        }
                ) as ChatMessageBo?
            }
            message?.let {
                val chatUserWithMessage = DBChatUserWithMessage.findOne {
                    it.messageId eq message.messageId
                }!!
                val user = ChatUserBo().getBo(DBChatUser.findById(chatUserWithMessage.userId)!!) as ChatUserBo

                chatGroup.lastMessage = ChatMessageInfoBo(
                        user = user,
                        createTime = DateUtils.format(chatUserWithMessage.gmtCreate),
                        message = message
                )
            }
            chatGroup
        }
    }

    override fun getGroupById(groupId: Int): ChatGroupBo {
        return DBChatGroup.findById(groupId)?.let {
            ChatGroupBo().getBo(it) as ChatGroupBo
        } ?: throw BaseException("群组不存在")
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

    override fun updateGroupLastTimeline(userId: Int, sessionId: Int, lastMessageId: Int, lastTimeline: Int): ChatGroupBo {
        DBChatGroup.update {
            it.lastMessageId to lastMessageId
            it.lastTimeline to lastTimeline
            where {
                it.groupId eq sessionId
            }
        }
        val session = DBChatGroup.findById(sessionId)!!
        return ChatGroupBo().getBo(session) as ChatGroupBo
    }

}