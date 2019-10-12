package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findListByIds
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/message")
    @ApiOperation(value = "获取消息数据")
    open override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val message = DBChatMessage.findById(messageId)?.let {
            ChatMessage(it.sessionId, it.id, it.sequence, ChatMessage.jsonToContent(it.content))
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
    open override fun getMessageListBySessionId(sessionId: Int): MResult<List<ChatMessage>> {
        val list = DBChatMessage.findList {
            it.sessionId eq sessionId
        }.map {
            ChatMessage(it.sessionId, it.id, it.sequence, ChatMessage.jsonToContent(it.content))
        }
        return MResult<List<ChatMessage>>().result(list)
    }

    @GetMapping("/message/getMessageListByUserId")
    @ApiOperation(value = "获取用户发送的消息")
    open override fun getMessageListByUserId(userId: Int): MResult<List<ChatMessage>> {
        val list = DBChatUserWithMessage.findList {
            it.userId eq userId
        }.map {
            it.messageId
        }
        var messages: List<ChatMessage>? = null
        if (list.isNotEmpty()) {
            messages = DBChatMessage.findListByIds(list).map {
                ChatMessage(sessionId = it.sessionId, messageId = it.id, sequence = it.sequence, content = ChatMessage.jsonToContent(it.content))
            }
        }
        return if (messages == null) {
            MResult<List<ChatMessage>>().error(500, "无法找到")
        } else {
            MResult<List<ChatMessage>>().result(messages)
        }
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
        message.content = ChatContent()
        val map = HashMap<String, Any?>()
        map["contentType"] = 0
        map["content"] = "测试消息内容"
        message.content.fields = map
        if (message.messageId < 0) {
            // 插入
            val messages = DBChatMessage.findList {
                it.sessionId eq message.sessionId
            }.map {
                ChatMessage(it.sessionId, it.id, it.sequence, ChatMessage.jsonToContent(it.content))
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
            }
        } else {
            // 更新
            DBChatMessage.update {
                it.content to message.contentToJson()

                where {
                    it.id eq message.messageId
                }
            }
        }
        return MResult<ChatMessage>().result(message)
    }

    @PutMapping("/session/createSession")
    @ApiOperation(value = "创建会话")
    open override fun createSession(userIdList: List<Int>, message: ChatMessage?): MResult<ChatSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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