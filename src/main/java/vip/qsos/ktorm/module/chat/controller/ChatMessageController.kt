package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.findList
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.tweet.entity.DBEmployees
import vip.qsos.ktorm.util.MResult

@RestController(value = "/chat")
class ChatMessageController : IChatModelConfig {

    @GetMapping("/session")
    @ApiOperation(value = "测试接口")
    override fun getSessionById(sessionId: Long): MResult<ChatSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/message")
    @ApiOperation(value = "测试接口")
    override fun getMessageById(messageId: Long): MResult<ChatMessage> {
        val one = DBChatMessage.findOne {
            it.messageId eq messageId
        }?.let {
            ChatMessage(it.sessionId, it.messageId, it.sequence, ChatMessage.jsonToContent(it.content))
        }
        return if (one == null) {
            MResult<ChatMessage>().error(500, "无法找到")
        } else {
            MResult<ChatMessage>().result(one)
        }
    }

    @GetMapping("/user")
    @ApiOperation(value = "测试接口")
    override fun getUserById(userId: Long): MResult<ChatUser> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/group")
    @ApiOperation(value = "测试接口")
    override fun getGroupById(groupId: Long): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/content")
    @ApiOperation(value = "测试接口")
    override fun getContentById(contentId: Long): MResult<ChatContent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/group/getGroupByBySessionId")
    @ApiOperation(value = "测试接口")
    override fun getGroupByBySessionId(sessionId: Long): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/user/getUserListBySessionId")
    @ApiOperation(value = "测试接口")
    override fun getUserListBySessionId(sessionId: Long): MResult<List<ChatUser>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/message/getMessageListBySessionId")
    @ApiOperation(value = "测试接口")
    override fun getMessageListBySessionId(sessionId: Long): MResult<List<ChatMessage>> {
        val list = DBChatMessage.findList {
            it.sessionId eq sessionId
        }.map {
            ChatMessage(it.sessionId, it.messageId, it.sequence, ChatMessage.jsonToContent(it.content))
        }
        return MResult<List<ChatMessage>>().result(list)
    }

    @GetMapping("/message/getMessageListByUserId")
    @ApiOperation(value = "测试接口")
    override fun getMessageListByUserId(userId: Long): MResult<List<ChatMessage>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/session/getSessionListByUserId")
    @ApiOperation(value = "测试接口")
    override fun getSessionListByUserId(userId: Long): MResult<List<ChatSession>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/message/sendMessage")
    @ApiOperation(value = "测试接口")
    override fun sendMessage(message: ChatMessage): MResult<ChatMessage> {
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
                it.messageId to message.messageId
                it.sequence to message.sequence
                it.content to message.contentToJson()
            }
            message.messageId = (mId as Int).toLong()
        } else {
            // 更新
            DBChatMessage.update {
                it.content to message.contentToJson()

                where {
                    it.id eq message.messageId.toInt()
                }
            }
        }
        return MResult<ChatMessage>().result(message)
    }

    @PutMapping("/session/createSession")
    @ApiOperation(value = "测试接口")
    override fun createSession(userIdList: List<Long>, message: ChatMessage?): MResult<ChatSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/session/addUserListToSession")
    @ApiOperation(value = "测试接口")
    override fun addUserListToSession(userIdList: List<Long>, sessionId: Long): MResult<ChatSession> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupNotice")
    @ApiOperation(value = "测试接口")
    override fun updateGroupNotice(notice: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupName")
    @ApiOperation(value = "测试接口")
    override fun updateGroupName(name: String): MResult<ChatGroup> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PostMapping("/session")
    @ApiOperation(value = "测试接口")
    override fun deleteSession(sessionId: Long): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "测试接口")
    override fun deleteUser(sessionId: Long, userId: Long): MResult<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @DeleteMapping("/message")
    @ApiOperation(value = "测试接口")
    override fun deleteMessage(messageId: Long): MResult<Boolean> {
        DBEmployees.delete { it.managerId.eq(messageId.toInt()) }
        return MResult<Boolean>().result(true)
    }

}