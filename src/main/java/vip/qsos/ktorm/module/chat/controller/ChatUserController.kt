package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatUserBo
import vip.qsos.ktorm.module.chat.entity.form.FormCreateUser
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatUserController(
        @Autowired
        private val mChatUserService: IChatService.IUser
) : IChatController.IUser {

    override fun createUser(user: FormCreateUser): MResult<ChatUserBo> {
        val result = mChatUserService.createUser(user)
        return MResult<ChatUserBo>().result(result)
    }

    override fun getAllUser(userId: Int): MResult<List<ChatUserBo>> {
        val result = mChatUserService.getAllUser(userId)
        return MResult<List<ChatUserBo>>().result(result)
    }

    override fun getUserById(userId: Int): MResult<ChatUserBo> {
        val result = mChatUserService.getUserById(userId)
        return MResult<ChatUserBo>().result(result)
    }

    override fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUserBo>> {
        val result = mChatUserService.getUserListBySessionId(sessionId)
        return MResult<List<ChatUserBo>>().result(result)
    }

    override fun deleteUser(userId: Int): MResult<Boolean> {
        val result = mChatUserService.deleteUser(userId)
        return MResult<Boolean>().result(result)
    }

}