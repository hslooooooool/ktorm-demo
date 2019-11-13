package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findListByIds
import me.liuwj.ktorm.entity.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vip.qsos.ktorm.config.ChatMessageProperties
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.util.DateUtils
import java.time.Duration
import java.time.LocalDateTime

@Service
class ChatMessageService @Autowired constructor(
        private val mChatGroupService: IChatService.IGroup,
        private val mChatMessageProperties: ChatMessageProperties
) : IChatService.IMessage {

    override fun getMessageById(messageId: Int): ChatMessageBo {
        return ChatMessageBo().getBo(DBChatMessage.findById(messageId)) as ChatMessageBo?
                ?: throw BaseException("消息不存在")
    }

    override fun getMessageListByIds(messageIds: List<Int>): List<ChatMessageBo> {
        return DBChatMessage.findListByIds(messageIds).map {
            ChatMessageBo().getBo(it) as ChatMessageBo
        }
    }

    override fun getMessageListBySessionId(sessionId: Int): List<ChatMessageInfoBo> {
        val list: ArrayList<ChatMessageInfoBo> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
            it.cancelBack eq false
        }.map { msg ->
            getDBChatUserWithMessage(msg, list)
        }
        return list
    }

    override fun getMessageListBySessionIdAndTimeline(sessionId: Int, timeline: Int): List<ChatMessageInfoBo> {
        val list: ArrayList<ChatMessageInfoBo> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
        }
        DBChatMessage.select().where {
            DBChatMessage.sessionId eq sessionId
        }.having {
            DBChatMessage.sequence greater timeline
        }.map { row ->
            val msg = TableChatMessage(
                    messageId = row[DBChatMessage.messageId]!!,
                    sessionId = row[DBChatMessage.sessionId] ?: -1,
                    sequence = row[DBChatMessage.sequence] ?: -1,
                    content = row[DBChatMessage.content] ?: "",
                    gmtCreate = row[DBChatMessage.gmtCreate]!!,
                    gmtUpdate = row[DBChatMessage.gmtUpdate]!!,
                    deleted = row[DBChatMessage.deleted]!!
            )
            getDBChatUserWithMessage(msg, list)
        }
        return list
    }

    private fun getDBChatUserWithMessage(msg: TableChatMessage, list: ArrayList<ChatMessageInfoBo>): ArrayList<ChatMessageInfoBo> {
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
        list.sortBy {
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
        val group = mChatGroupService.getGroupById(message.sessionId)
        val lastTimeline = group.lastTimeline + 1
        if (message.messageId > 0) {
            // 更新
            DBChatMessage.update {
                it.cancelBack to true

                where {
                    it.messageId eq message.messageId
                }
            }
        }
        // 插入
        message.messageId = -1
        message.sequence = lastTimeline
        val mId = DBChatMessage.add(message.toTable()) as Int
        message.messageId = mId

        DBChatUserWithMessage.add(TableChatUserWithMessage(
                userId = userId,
                messageId = mId
        ))
        mChatGroupService.updateGroupLastTimeline(userId, message.sessionId, mId, lastTimeline)
        return message
    }

    override fun deleteMessage(messageId: Int): Boolean {
        val msg = DBChatMessage.findById(messageId)
        return when {
            msg == null -> false
            Duration.between(msg.gmtCreate, LocalDateTime.now())
                    .seconds > mChatMessageProperties.cancellimit -> false
            else -> {
                val result = DBChatMessage.update {
                    it.cancelBack to true
                    where {
                        it.messageId eq messageId
                    }
                }
                result == 1
            }
        }
    }

}