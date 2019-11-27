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

    override fun getMessageById(userId: Int, messageId: Int): ChatMessageBo {
        return ChatMessageBo().getBo(DBChatMessage.findById(messageId)) as ChatMessageBo?
                ?: throw BaseException("消息不存在")
    }

    override fun getMessageListByIds(userId: Int, messageIds: List<Int>): List<ChatMessageBo> {
        return DBChatMessage.findListByIds(messageIds).map {
            ChatMessageBo().getBo(it) as ChatMessageBo
        }
    }

    override fun getMessageListBySessionId(userId: Int, sessionId: Int): List<ChatMessageInfoBo> {
        val list: ArrayList<ChatMessageInfoBo> = arrayListOf()
        DBChatMessage.select()
                .where {
                    (DBChatMessage.sessionId eq sessionId) and (DBChatMessage.cancelBack eq false)
                }.map {
                    getDBChatUserWithMessage(userId, DBChatMessage.createEntity(it), list)
                }
        return list
    }

    override fun getMessageListBySessionIdAndTimeline(userId: Int, sessionId: Int, timeline: Int): List<ChatMessageInfoBo> {
        val list: ArrayList<ChatMessageInfoBo> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
        }
        DBChatMessage.select().where {
            DBChatMessage.sessionId eq sessionId
        }.having {
            DBChatMessage.timeline greater timeline
        }.map { row ->
            val msg = TableChatMessage(
                    messageId = row[DBChatMessage.messageId]!!,
                    sessionId = row[DBChatMessage.sessionId] ?: -1,
                    timeline = row[DBChatMessage.timeline]!!,
                    content = row[DBChatMessage.content] ?: "",
                    gmtCreate = row[DBChatMessage.gmtCreate]!!,
                    gmtUpdate = row[DBChatMessage.gmtUpdate]!!,
                    deleted = row[DBChatMessage.deleted]!!
            )
            getDBChatUserWithMessage(userId, msg, list)
        }
        return list
    }

    private fun getDBChatUserWithMessage(userId: Int, msg: TableChatMessage, list: ArrayList<ChatMessageInfoBo>): ArrayList<ChatMessageInfoBo> {
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
                val read = getMessageReadStatus(userId, msg.messageId)
                list.add(ChatMessageInfoBo(
                        user = user, message = message, createTime = createTime,
                        readNum = read.readNum, readStatus = read.readStatus
                ))
            }
        }
        list.sortBy {
            it.message.timeline
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
            val read = getMessageReadStatus(userId, it.messageId)
            messages.add(ChatMessageInfoBo(
                    user = user, message = message, createTime = createTime,
                    readNum = read.readNum, readStatus = read.readStatus)
            )
        }
        messages.sortByDescending {
            it.message.timeline
        }
        return messages
    }

    override fun addUserWithMessage(userId: Int, messageId: Int): Boolean {
        val result = DBChatUserWithMessage.add(TableChatUserWithMessage(
                userId = userId,
                messageId = messageId
        )) as Int
        return result == 1
    }

    override fun addMessageReadStatus(userId: Int, messageId: Int): Boolean {
        val result = DBChatMessageReadStatus.add(TableChatMessageReadStatus(
                messageId = messageId,
                readIds = "_$userId"
        )) as Int
        return result == 1
    }

    override fun getMessageReadStatus(userId: Int, messageId: Int): IChatService.IMessage.MessageReadStatus {
        val read = DBChatMessageReadStatus.findById(messageId)
                ?: throw BaseException("消息读取记录不存在")
        val readStatus = read.readIds.contains("$userId")
        val readNum = read.readIds.split("_").size
        return IChatService.IMessage.MessageReadStatus(readStatus, readNum)
    }

    override fun sendMessage(userId: Int, message: ChatMessageBo): ChatMessageBo {
        if (message.sessionId < 0) {
            throw BaseException("会话不存在，发送失败")
        }
        val group = mChatGroupService.getGroupById(message.sessionId)
        val lastTimeline = (if (group.lastTimeline < 0) 0 else group.lastTimeline) + 1
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
        message.timeline = lastTimeline
        val mId = DBChatMessage.add(message.toTable()) as Int
        message.messageId = mId

        if (!addUserWithMessage(userId, mId)) throw BaseException("消息与用户关系添加失败")
        if (!addMessageReadStatus(userId, mId)) throw BaseException("消息记录添加失败")

        mChatGroupService.updateGroupLastTimeline(userId, message.sessionId, mId, lastTimeline)
        return message
    }

    override fun readMessage(userId: Int, messageId: Int): Boolean {
        var result = 1
        val read = DBChatMessageReadStatus.findById(messageId)
                ?: throw BaseException("消息读取记录不存在")
        if (!read.readIds.contains("$userId")) {
            val readIds = read.readIds + "_$userId"
            result = DBChatMessageReadStatus.update {
                it.readIds to readIds
                where {
                    it.messageId eq messageId
                }
            }
        }
        return result == 1
    }

    override fun deleteMessage(userId: Int, messageId: Int): Boolean {
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