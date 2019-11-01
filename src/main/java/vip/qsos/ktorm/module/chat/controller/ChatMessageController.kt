package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.*
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.tweet.entity.DBEmployees
import vip.qsos.ktorm.util.MResult

@RestController
@RequestMapping("/chat")
open class ChatMessageController : IChatModelConfig {

    override fun getSessionById(sessionId: Int): MResult<ChatSessionBo> {
        val session = DBChatSession.findById(sessionId)?.let {
            ChatSessionBo(sessionId = it.sessionId, type = it.type)
        }
        return if (session == null) {
            MResult<ChatSessionBo>().error(500, "无法找到")
        } else {
            MResult<ChatSessionBo>().result(session)
        }
    }

    override fun getAllUser(userId: Int): MResult<List<ChatUserBo>> {
        val users = DBChatUser.findAll()
                .map {
                    ChatUserBo(
                            userId = it.userId,
                            userName = it.userName,
                            avatar = it.avatar,
                            birth = it.birth,
                            sexuality = it.sexuality
                    )
                }
        return MResult<List<ChatUserBo>>().result(users)
    }

    override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val message = ChatMessage.getVo(DBChatMessage.findById(messageId))
        return if (message == null) {
            MResult<ChatMessage>().error(500, "无法找到")
        } else {
            MResult<ChatMessage>().result(message)
        }
    }

    override fun getGroupWithMe(userId: Int): MResult<List<ChatGroupBo>> {
        val groupIds = DBChatUserWithSession.findList {
            it.userId eq userId
        }.map {
            it.sessionId
        }.toSet()
        val groupList = DBChatGroup.findListByIds(groupIds).map { group ->
            val chatGroup = ChatGroupBo.getVo(group)

            val message = group.lastMessageId?.let { messageId ->
                ChatMessage.getVo(DBChatMessage.findOne {
                    it.messageId eq messageId
                })
            }
            message?.let {
                val chatUserWithMessage = DBChatUserWithMessage.findOne {
                    it.messageId eq message.messageId
                }!!
                val user = ChatUserBo.getBo(DBChatUser.findById(chatUserWithMessage.userId)!!)

                chatGroup.lastMessage = ChatMessageBo(
                        user = user,
                        createTime = chatUserWithMessage.gmtCreate,
                        message = message
                )
            }
            chatGroup
        }
        return MResult<List<ChatGroupBo>>().result(groupList)
    }

    override fun getUserById(userId: Int): MResult<ChatUserBo> {
        var user: ChatUserBo? = null
        DBChatUser.findById(userId)?.let {
            user = ChatUserBo(
                    userId = it.userId, userName = it.userName, avatar = it.avatar,
                    birth = it.birth, sexuality = it.sexuality
            )
        }
        return if (user == null) {
            MResult<ChatUserBo>().error(500, "无法找到")
        } else {
            MResult<ChatUserBo>().result(user!!)
        }
    }

    @GetMapping("/group")
    @ApiOperation(value = "获取群信息")
    override fun getGroupById(groupId: Int): MResult<ChatGroupBo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @GetMapping("/group/getGroupByBySessionId")
    @ApiOperation(value = "获取会话对应的群信息")
    override fun getGroupByBySessionId(sessionId: Int): MResult<ChatGroupBo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUserBo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMessageListBySessionId(sessionId: Int): MResult<List<ChatMessageBo>> {
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
        return MResult<List<ChatMessageBo>>().result(list)
    }

    override fun getMessageListByUserId(userId: Int): MResult<List<ChatMessageBo>> {
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
        return MResult<List<ChatMessageBo>>().result(messages)
    }

    override fun getSessionListByUserId(userId: Int): MResult<List<ChatSessionBo>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUser(user: ChatUserBo): MResult<ChatUserBo> {
        val id = DBChatUser.add(user.toTable())
        user.userId = id as Int
        return MResult<ChatUserBo>().result(user)
    }

    override fun sendMessage(userId: Int, message: ChatMessage): MResult<ChatMessage> {
        if (message.sessionId < 0) {
            return MResult<ChatMessage>().error(500, "会话不存在，发送失败")
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
        return MResult<ChatMessage>().result(message)
    }

    override fun createSession(userId: Int, data: IChatModel.Post.FormCreateSession): MResult<ChatSessionBo> {

        val oldSession = hasSession(data.userIdList)
        if (oldSession.sessionId != -1) {
            // 存在已有群，直接获取
            return MResult<ChatSessionBo>().result(oldSession)
        }

        val createTime = System.currentTimeMillis()

        val session = ChatSessionBo(
                type = if (data.userIdList.size > 2) ChatType.GROUP else ChatType.SINGLE,
                hashCode = oldSession.hashCode
        )
        val sessionId = DBChatSession.add(session.toTable()) as Int
        session.sessionId = sessionId
        DBChatGroup.add(TableChatGroup(
                groupId = sessionId,
                name = "$createTime【群】",
                notice = "",
                avatar = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
        ))

        data.userIdList.forEach { uId ->
            DBChatUserWithSession.add(TableChatUserWithSession(
                    userId = uId,
                    sessionId = sessionId
            ))
        }
        data.message?.let {
            it.sessionId = sessionId
            val result = sendMessage(userId, it)
            return if (result.code == 200) {
                MResult<ChatSessionBo>().result(session)
            } else {
                MResult<ChatSessionBo>().error(result.code, result.msg)
            }
        }
        return MResult<ChatSessionBo>().result(session)
    }

    override fun hasSession(userIdList: List<Int>): ChatSessionBo {
        var hashCode = ""
        userIdList.sorted().forEach {
            hashCode += it.toString()
        }
        hashCode = hashCode.hashCode().toString()
        return DBChatSession.findOne {
            it.hashCode eq hashCode
        }?.let {
            ChatSessionBo(sessionId = it.sessionId, type = it.type, hashCode = it.hashCode)
        } ?: ChatSessionBo(sessionId = -1, type = ChatType.SINGLE, hashCode = hashCode)
    }

    override fun addUserListToSession(userId: Int, userIdList: List<Int>, sessionId: Int): MResult<ChatSessionBo> {
        val session = DBChatSession.findById(sessionId)?.let {
            ChatSessionBo(sessionId = it.sessionId, type = it.type)
        } ?: return MResult<ChatSessionBo>().error(500, "会话不存在")
        userIdList.forEach { uId ->
            DBChatUserWithSession.add(TableChatUserWithSession(
                    userId = uId,
                    sessionId = sessionId
            ))
        }
        return MResult<ChatSessionBo>().result(session)
    }

    @PutMapping("/group/updateGroupNotice")
    @ApiOperation(value = "更新群公告")
    override fun updateGroupNotice(notice: String): MResult<ChatGroupBo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @PutMapping("/group/updateGroupName")
    @ApiOperation(value = "更新群名称")
    override fun updateGroupName(name: String): MResult<ChatGroupBo> {
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