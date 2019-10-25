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

    @GetMapping("/session")
    @ApiOperation(value = "获取会话信息")
    open override fun getSessionById(sessionId: Int): MResult<ChatSession> {
        var session: ChatSession? = null
        DBChatSession.findById(sessionId)?.let {
            session = ChatSession(sessionId = it.sessionId, type = ChatType.valueOf(it.type))
        }
        return if (session == null) {
            MResult<ChatSession>().error(500, "无法找到")
        } else {
            MResult<ChatSession>().result(session!!)
        }
    }

    @GetMapping("/message")
    @ApiOperation(value = "获取消息数据")
    open override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val message = DBChatMessage.findById(messageId)?.let {
            ChatMessage(it.sessionId, it.messageId, it.sequence, ChatMessage.jsonToContent(it.content))
        }
        return if (message == null) {
            MResult<ChatMessage>().error(500, "无法找到")
        } else {
            MResult<ChatMessage>().result(message)
        }
    }

    @GetMapping("/user")
    @ApiOperation(value = "获取用户信息")
    open override fun getUserById(userId: Int): MResult<ChatUser> {
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
    open override fun getGroupById(groupId: Int): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/group/getGroupByBySessionId")
    @ApiOperation(value = "获取会话对应的群信息")
    open override fun getGroupByBySessionId(sessionId: Int): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/user/getUserListBySessionId")
    @ApiOperation(value = "获取会话下的用户列表")
    open override fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUser>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/message/getMessageListBySessionId")
    @ApiOperation(value = "获取会话下的消息列表")
    open override fun getMessageListBySessionId(sessionId: Int): MResult<List<MChatMessage>> {
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

    @GetMapping("/message/getMessageListByUserId")
    @ApiOperation(value = "获取用户发送的消息")
    open override fun getMessageListByUserId(userId: Int): MResult<List<MChatMessage>> {
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

    @GetMapping("/session/getSessionListByUserId")
    @ApiOperation(value = "获取用户订阅的会话")
    open override fun getSessionListByUserId(userId: Int): MResult<List<ChatSession>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/user/createUser")
    @ApiOperation(value = "创建用户")
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

    @PostMapping("/message/sendMessage")
    @ApiOperation(value = "发送消息")
    open override fun sendMessage(userId: Int, message: ChatMessage): MResult<ChatMessage> {
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

    @PutMapping("/session/createSession")
    @ApiOperation(value = "创建会话")
    open override fun createSession(userId: Int, userIdList: List<Int>, message: ChatMessage?): MResult<ChatSession> {
        val session = ChatSession(
                type = if (userIdList.size > 1) ChatType.GROUP else ChatType.SINGLE
        )
        val id = DBChatSession.insertAndGenerateKey {
            it.type to session.type
        }
        session.sessionId = id as Int
        message?.let {
            it.sessionId = id
            val result = sendMessage(userId, it)
            return if (result.code == 200) {
                MResult<ChatSession>().result(session)
            } else {
                MResult<ChatSession>().error(result.code, result.msg)
            }
        }
        return MResult<ChatSession>().result(session)
    }

    @PostMapping("/session/addUserListToSession")
    @ApiOperation(value = "会话中增加用户")
    open override fun addUserListToSession(userIdList: List<Int>, sessionId: Int): MResult<ChatSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupNotice")
    @ApiOperation(value = "更新群公告")
    open override fun updateGroupNotice(notice: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupName")
    @ApiOperation(value = "更新群名称")
    open override fun updateGroupName(name: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/session")
    @ApiOperation(value = "删除会话")
    open override fun deleteSession(sessionId: Int): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "删除用户")
    open override fun deleteUser(sessionId: Int, userId: Int): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @DeleteMapping("/message")
    @ApiOperation(value = "删除(撤销)消息")
    open override fun deleteMessage(messageId: Int): MResult<Boolean> {
        DBEmployees.delete { it.managerId.eq(messageId.toInt()) }
        return MResult<Boolean>().result(true)
    }

}