package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.tweet.entity.DBEmployees
import vip.qsos.ktorm.util.MResult

@RestController
@RequestMapping("/chat")
open class ChatMessageController : IChatModelConfig {

    override fun getSessionById(sessionId: Int): MResult<ChatSession> {
        val session = DBChatSession.findById(sessionId)?.let {
            ChatSession(sessionId = it.sessionId, type = it.type)
        }
        return if (session == null) {
            MResult<ChatSession>().error(500, "无法找到")
        } else {
            MResult<ChatSession>().result(session)
        }
    }

    override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val message = DBChatMessage.findById(messageId)?.let {
            ChatMessage(it.sessionId, it.messageId, it.sequence, ChatMessage.jsonToContent(it.content))
        }
        return if (message == null) {
            MResult<ChatMessage>().error(500, "无法找到")
        } else {
            MResult<ChatMessage>().result(message)
        }
    }

    override fun getUserById(userId: Int): MResult<ChatUser> {
        var user: ChatUser? = null
        DBChatUser.findById(userId)?.let {
            user = ChatUser(userId = it.userId, userName = it.userName, avatar = it.avatar, birth = it.birth, sexuality = it.sexuality)
        }
        return if (user == null) {
            MResult<ChatUser>().error(500, "无法找到")
        } else {
            MResult<ChatUser>().result(user!!)
        }
    }

    @GetMapping("/group")
    @ApiOperation(value = "获取群信息")
    override fun getGroupById(groupId: Int): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/group/getGroupByBySessionId")
    @ApiOperation(value = "获取会话对应的群信息")
    override fun getGroupByBySessionId(sessionId: Int): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUser>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMessageListBySessionId(sessionId: Int): MResult<List<MChatMessage>> {
        val list: ArrayList<MChatMessage> = arrayListOf()
        DBChatMessage.findList {
            it.sessionId eq sessionId
        }.map { msg ->
            DBChatUserWithMessage.findOne {
                it.messageId eq msg.messageId
            }?.let { v ->
                val createTime = v.createTime
                val message = ChatMessage(msg.sessionId, msg.messageId, msg.sequence, ChatMessage.jsonToContent(msg.content))
                DBChatUser.findOne {
                    it.userId eq v.userId
                }?.let {
                    ChatUser(userId = it.userId, userName = it.userName, avatar = it.avatar, birth = it.birth,
                            sexuality = it.sexuality)
                }?.let { user ->
                    list.add(MChatMessage(user = user, message = message, createTime = createTime))
                }
            }
        }
        list.sortByDescending {
            it.message.sequence
        }
        return MResult<List<MChatMessage>>().result(list)
    }

    override fun getMessageListByUserId(userId: Int): MResult<List<MChatMessage>> {
        val list = DBChatUserWithMessage.findList {
            it.userId eq userId
        }
        val messages: ArrayList<MChatMessage> = arrayListOf()
        list.forEach {
            val createTime = it.createTime
            val user = DBChatUser.findById(it.userId)!!.let { user ->
                ChatUser(userId = user.userId, userName = user.userName, avatar = user.avatar, birth = user.birth,
                        sexuality = user.sexuality)
            }
            val message = DBChatMessage.findById(it.messageId)!!.let { msg ->
                ChatMessage(sessionId = msg.sessionId, messageId = msg.messageId, sequence = msg.sequence,
                        content = ChatMessage.jsonToContent(msg.content))
            }
            messages.add(MChatMessage(user = user, message = message, createTime = createTime))
        }
        messages.sortByDescending {
            it.message.sequence
        }
        return MResult<List<MChatMessage>>().result(messages)
    }

    override fun getSessionListByUserId(userId: Int): MResult<List<ChatSession>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUser(user: ChatUser): MResult<ChatUser> {
        val id = DBChatUser.insertAndGenerateKey {
            it.userName to user.userName
            it.avatar to user.avatar
            it.birth to user.birth
            it.sexuality to user.sexuality
        }
        user.userId = id as Int
        return MResult<ChatUser>().result(user)
    }

    override fun sendMessage(userId: Int, message: ChatMessage): MResult<ChatMessage> {
        if (message.messageId < 0) {
            // 插入
            val messages = DBChatMessage.findList {
                it.sessionId eq message.sessionId
            }.map {
                ChatMessage(it.sessionId, it.messageId, it.sequence, ChatMessage.jsonToContent(it.content))
            }.sortedBy {
                it.sequence
            }
            if (messages.isNotEmpty()) {
                message.sequence = messages.last().sequence + 1
                message.messageId = messages.last().messageId + 1
            }
            val mId = DBChatMessage.insertAndGenerateKey {
                it.sessionId to message.sessionId
                it.sequence to message.sequence
                it.content to message.contentToJson()
            }
            message.messageId = mId as Int

            DBChatUserWithMessage.insert {
                it.userId to userId
                it.messageId to mId
                it.createTime to System.currentTimeMillis()
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
        return MResult<ChatMessage>().result(message)
    }

    override fun createSession(userId: Int, data: IChatModel.Post.FormCreateSession): MResult<ChatSession> {
        val session = ChatSession(
                type = if (data.userIdList.size > 1) ChatType.GROUP else ChatType.SINGLE
        )
        val sessionId = DBChatSession.insertAndGenerateKey {
            it.type to session.type.ordinal
        }
        session.sessionId = sessionId as Int
        data.userIdList.forEach { id ->
            DBChatUserWithSession.insert {
                it.sessionId to session.sessionId
                it.userId to id
                it.createTime to System.currentTimeMillis()
            }
        }
        data.message?.let {
            it.sessionId = sessionId
            val result = sendMessage(userId, it)
            return if (result.code == 200) {
                MResult<ChatSession>().result(session)
            } else {
                MResult<ChatSession>().error(result.code, result.msg)
            }
        }
        return MResult<ChatSession>().result(session)
    }

    override fun addUserListToSession(userId: Int, userIdList: List<Int>, sessionId: Int): MResult<ChatSession> {
        val session = DBChatSession.findById(sessionId)?.let {
            ChatSession(sessionId = it.sessionId, type = it.type)
        } ?: return MResult<ChatSession>().error(500, "会话不存在")
        userIdList.forEach { id ->
            DBChatUserWithSession.insert {
                it.sessionId to session.sessionId
                it.userId to id
                it.createTime to System.currentTimeMillis()
            }
        }
        return MResult<ChatSession>().result(session)
    }

    @PutMapping("/group/updateGroupNotice")
    @ApiOperation(value = "更新群公告")
    override fun updateGroupNotice(notice: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupName")
    @ApiOperation(value = "更新群名称")
    override fun updateGroupName(name: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/session")
    @ApiOperation(value = "删除会话")
    override fun deleteSession(sessionId: Int): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "删除用户")
    override fun deleteUser(sessionId: Int, userId: Int): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteMessage(messageId: Int): MResult<Boolean> {
        DBEmployees.delete { it.managerId.eq(messageId.toInt()) }
        return MResult<Boolean>().result(true)
    }

}