package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.findById
import me.liuwj.ktorm.entity.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.chat.entity.form.FormCreateSession

/**
 * @author : 华清松
 * @date : 2019/11/2
 * @description : 聊天消息服务
 */
@Service
class ChatSessionService(
        @Autowired
        private val mChatMessageService: IChatService.IMessage
) : IChatService.ISession {

    override fun getSessionById(sessionId: Int): ChatSessionBo {
        return DBChatSession.findById(sessionId)?.let {
            ChatSessionBo(sessionId = it.sessionId, type = it.type)
        } ?: throw BaseException("无法找到")
    }

    override fun getSessionListByUserId(userId: Int): List<ChatSessionBo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSession(userId: Int, data: FormCreateSession): ChatSessionBo {
        val oldSession = hasSession(data.userIdList)
        if (oldSession.sessionId != -1) {
            // 存在已有群，直接获取
            return oldSession
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
            mChatMessageService.sendMessage(userId, it)
            return session
        }
        return session
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

    override fun addUserListToSession(userId: Int, userIdList: List<Int>, sessionId: Int): ChatSessionBo {
        val session = DBChatSession.findById(sessionId)?.let {
            ChatSessionBo(sessionId = it.sessionId, type = it.type)
        } ?: throw BaseException("会话不存在")
        userIdList.forEach { uId ->
            DBChatUserWithSession.add(TableChatUserWithSession(
                    userId = uId,
                    sessionId = sessionId
            ))
        }
        return session
    }

    override fun deleteSession(sessionId: Int): Boolean {
        val result = DBChatSession.delete {
            it.sessionId eq sessionId
        }
        return result == 1
    }

}