package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findOne
import org.springframework.stereotype.Service
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.util.DateUtils

@Service
class ChatMessageService : IChatService.IMessage {

    override fun getMessageById(messageId: Int): ChatMessageBo {
        return ChatMessageBo().getBo(DBChatMessage.findById(messageId)) as ChatMessageBo?
                ?: throw BaseException("消息不存在")
    }

    override fun getMessageListBySessionId(sessionId: Int): List<ChatMessageInfoBo> {
        val list: ArrayList<ChatMessageInfoBo> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
        }.map { msg ->
            DBChatUserWithMessage.findOne {
                it.messageId eq msg.messageId
            }?.let { v ->
                val createTime = DateUtils.format(v.gmtCreate)
                val message = ChatMessageBo().getBo(msg) as ChatMessageBo
                DBChatUser.findOne {
                    it.userId eq v.userId
                }?.let {
                    ChatUserBo(
                            userId = it.userId, userName = it.userName, avatar = it.avatar,
                            birth = it.birth, sexuality = it.sexuality
                    )
                }?.let { user ->
                    list.add(ChatMessageInfoBo(user = user, message = message, createTime = createTime))
                }
            }
        }
        list.sortByDescending {
            it.message.sequence
        }
        return list
    }

    override fun getMessageListByUserId(userId: Int): List<ChatMessageInfoBo> {
        val list = DBChatUserWithMessage.findList {
            it.userId eq userId
        }
        val messages: ArrayList<ChatMessageInfoBo> = arrayListOf()
        list.forEach {
            val createTime = DateUtils.format(it.gmtCreate)
            val user = ChatUserBo().getBo(DBChatUser.findById(it.userId)) as ChatUserBo
            val message = ChatMessageBo().getBo(DBChatMessage.findById(it.messageId)) as ChatMessageBo
            messages.add(ChatMessageInfoBo(user = user, message = message, createTime = createTime))
        }
        messages.sortByDescending {
            it.message.sequence
        }
        return messages
    }

    override fun sendMessage(userId: Int, message: ChatMessageBo): ChatMessageBo {
        if (message.sessionId < 0) {
            throw BaseException("会话不存在，发送失败")
        }
        if (message.messageId < 0) {
            // 插入
            val mId = DBChatMessage.add(message.toTable())
            message.messageId = mId as Int

            DBChatUserWithMessage.add(TableChatUserWithMessage(
                    userId = userId,
                    messageId = mId
            ))

            DBChatGroup.update {
                it.lastMessageId to mId

                where {
                    it.groupId eq message.sessionId
                }
            }
        } else {
            // 更新
            DBChatMessage.update {
                it.content to message.contentToJson()

                where {
                    it.messageId eq message.messageId
                }
            }
        }
        return message
    }

    override fun deleteMessage(messageId: Int): Boolean {
        val result = DBChatMessage.delete {
            it.messageId eq messageId
        }
        return result == 1
    }

}