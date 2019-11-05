package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatSessionBo
import vip.qsos.ktorm.module.chat.entity.form.FormCreateSession
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatSessionController @Autowired constructor(
        private val mChatSessionService: IChatService.ISession
) : IChatController.ISession {

    override fun getSessionById(sessionId: Int): MResult<ChatSessionBo> {
        val result = mChatSessionService.getSessionById(sessionId)
        return MResult<ChatSessionBo>().result(result)
    }

    override fun getSessionListByUserId(userId: Int): MResult<List<ChatSessionBo>> {
        val result = mChatSessionService.getSessionListByUserId(userId)
        return MResult<List<ChatSessionBo>>().result(result)
    }

    override fun createSession(userId: Int, data: FormCreateSession): MResult<ChatSessionBo> {
        val result = mChatSessionService.createSession(userId, data)
        return MResult<ChatSessionBo>().result(result)
    }

    override fun addUserListToSession(userId: Int, userIdList: List<Int>, sessionId: Int): MResult<ChatSessionBo> {
        val result = mChatSessionService.addUserListToSession(userId, userIdList, sessionId)
        return MResult<ChatSessionBo>().result(result)
    }

    override fun deleteSession(sessionId: Int): MResult<Boolean> {
        val result = mChatSessionService.deleteSession(sessionId)
        return MResult<Boolean>().result(result)
    }

}