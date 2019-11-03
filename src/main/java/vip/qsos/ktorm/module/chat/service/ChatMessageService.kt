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
import vip.qsos.ktorm.module.tweet.entity.DBEmployees

/**
 * @author : 华清松
 * @date : 2019/11/2
 * @description : 聊天消息服务
 */
@Service
class ChatMessageService : IChatService.IMessage {

    override fun getMessageById(messageId: Int): ChatMessage {
        return ChatMessage.getVo(DBChatMessage.findById(messageId))
                ?: throw BaseException("无法找到")
    }

    override fun getMessageListBySessionId(sessionId: Int): List<ChatMessageBo> {
        val list: ArrayList<ChatMessageBo> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
        }.map { msg ->
            DBChatUserWithMessage.findOne {
                it.messageId eq msg.messageId
            }?.let { v ->
                val createTime = v.gmtCreate
                val message = ChatMessage.getVo(msg)!!
                DBChatUser.findOne {
                    it.userId eq v.userId
                }?.let {
                    ChatUserBo(
                            userId = it.userId, userName = it.userName, avatar = it.avatar,
                            birth = it.birth, sexuality = it.sexuality
                    )
                }?.let { user ->
                    list.add(ChatMessageBo(user = user, message = message, createTime = createTime))
                }
            }
        }
        list.sortByDescending {
            it.message.sequence
        }
        return list
    }

    override fun getMessageListByUserId(userId: Int): List<ChatMessageBo> {
        val list = DBChatUserWithMessage.findList {
            it.userId eq userId
        }
        val messages: ArrayList<ChatMessageBo> = arrayListOf()
        list.forEach {
            val createTime = it.gmtCreate
            val user = DBChatUser.findById(it.userId)!!.let { user ->
                ChatUserBo(userId = user.userId, userName = user.userName, avatar = user.avatar, birth = user.birth,
                        sexuality = user.sexuality)
            }
            val message = ChatMessage.getVo(DBChatMessage.findById(it.messageId))!!
            messages.add(ChatMessageBo(user = user, message = message, createTime = createTime))
        }
        messages.sortByDescending {
            it.message.sequence
        }
        return messages
    }

    override fun sendMessage(userId: Int, message: ChatMessage): ChatMessage {
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
        val result = DBEmployees.delete { it.managerId.eq(messageId.toInt()) }
        return result == 1
    }

}