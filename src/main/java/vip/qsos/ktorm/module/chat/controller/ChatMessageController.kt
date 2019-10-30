package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.*
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

    override fun getAllUser(userId: Int): MResult<List<ChatUser>> {
        val users = DBChatUser.findAll()
                .map {
                    ChatUser(
                            userId = it.userId,
                            userName = it.userName,
                            avatar = it.avatar,
                            birth = it.birth,
                            sexuality = it.sexuality
                    )
                }
        return MResult<List<ChatUser>>().result(users)
    }

    override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val message = DBChatMessage.findById(messageId)?.toChatMessage()
        return if (message == null) {
            MResult<ChatMessage>().error(500, "无法找到")
        } else {
            MResult<ChatMessage>().result(message)
        }
    }

    override fun getGroupWithMe(userId: Int): MResult<List<ChatGroup>> {
        val groupIds = DBChatUserWithSession.findList {
            it.userId eq userId
        }.map {
            it.sessionId
        }.toSet()
        val groupList = DBChatGroup.findListByIds(groupIds).map { group ->
            val chatGroup = ChatGroup(
                    groupId = group.groupId,
                    name = group.name,
                    createTime = group.createTime,
                    avatar = group.avatar,
                    notice = group.notice
            )
            chatGroup.lastMessage = group.lastMessageId?.let { messageId ->
                DBChatMessage.findOne {
                    it.messageId eq messageId
                }?.toChatMessage()
            }
            chatGroup
        }
        return MResult<List<ChatGroup>>().result(groupList)
    }

    override fun getUserById(userId: Int): MResult<ChatUser> {
        var user: ChatUser? = null
        DBChatUser.findById(userId)?.let {
            user = ChatUser(
                    userId = it.userId, userName = it.userName, avatar = it.avatar,
                    birth = it.birth, sexuality = it.sexuality
            )
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
                val message = msg.toChatMessage()
                DBChatUser.findOne {
                    it.userId eq v.userId
                }?.let {
                    ChatUser(
                            userId = it.userId, userName = it.userName, avatar = it.avatar,
                            birth = it.birth, sexuality = it.sexuality
                    )
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
            val message = DBChatMessage.findById(it.messageId)!!.toChatMessage()
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
        if (message.sessionId < 0) {
            return MResult<ChatMessage>().error(500, "会话不存在，发送失败")
        }
        if (message.messageId < 0) {
            // 插入
            val mId = DBChatMessage.insertAndGenerateKey {
                it.sessionId to message.sessionId
                it.content to message.contentToJson()
            }
            message.messageId = mId as Int

            DBChatUserWithMessage.insert {
                it.userId to userId
                it.messageId to mId
                it.createTime to System.currentTimeMillis()
            }

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
        return MResult<ChatMessage>().result(message)
    }

    override fun createSession(userId: Int, data: IChatModel.Post.FormCreateSession): MResult<ChatSession> {

        val oldSession = hasSession(data.userIdList)
        if (oldSession.sessionId != -1) {
            // 存在已有群，直接获取
            return MResult<ChatSession>().result(oldSession)
        }

        val createTime = System.currentTimeMillis()

        val session = ChatSession(
                type = if (data.userIdList.size > 2) ChatType.GROUP else ChatType.SINGLE
        )
        val sessionId = DBChatSession.insertAndGenerateKey {
            it.type to session.type.ordinal
            it.hashCode to oldSession.hashCode
        } as Int
        session.sessionId = sessionId

        DBChatGroup.insert {
            it.groupId to sessionId
            it.name to "$createTime【群】"
            it.createTime to createTime
            it.avatar to "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
        }

        data.userIdList.forEach { id ->
            DBChatUserWithSession.insert {
                it.sessionId to sessionId
                it.userId to id
                it.createTime to createTime
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

    override fun hasSession(userIdList: List<Int>): ChatSession {
        var hashCode = ""
        userIdList.sorted().forEach {
            hashCode += it.toString()
        }
        hashCode = hashCode.hashCode().toString()
        return DBChatSession.findOne {
            it.hashCode eq hashCode
        }?.let {
            ChatSession(sessionId = it.sessionId, type = it.type, hashCode = it.hashCode)
        } ?: ChatSession(sessionId = -1, type = ChatType.SINGLE, hashCode = hashCode)
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