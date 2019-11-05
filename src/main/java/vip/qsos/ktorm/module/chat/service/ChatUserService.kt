package vip.qsos.ktorm.module.chat.service

import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.findAll
import me.liuwj.ktorm.entity.findById
import org.springframework.stereotype.Service
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.chat.entity.ChatUserBo
import vip.qsos.ktorm.module.chat.entity.DBChatUser
import vip.qsos.ktorm.module.chat.entity.TableChatUser
import vip.qsos.ktorm.module.chat.entity.form.FormCreateUser

/**
 * @author : 华清松
 * @date : 2019/11/2
 * @description : 聊天消息服务
 */
@Service
class ChatUserService : IChatService.IUser {

    override fun getAllUser(userId: Int): List<ChatUserBo> {
        return DBChatUser.findAll()
                .map {
                    ChatUserBo(
                            userId = it.userId,
                            userName = it.userName,
                            avatar = it.avatar,
                            birth = it.birth,
                            sexuality = it.sexuality
                    )
                }
    }

    override fun getUserById(userId: Int): ChatUserBo {
        return ChatUserBo().getBo(DBChatUser.findById(userId)) as ChatUserBo?
                ?: throw BaseException("用户不存在")
    }

    override fun getUserListBySessionId(sessionId: Int): List<ChatUserBo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createUser(user: FormCreateUser): ChatUserBo {
        val result = TableChatUser(
                userName = user.userName,
                avatar = user.avatar,
                birth = user.birth,
                sexuality = user.sexuality
        )
        val id = DBChatUser.add(result) as Int
        result.userId = id
        return ChatUserBo().getBo(result) as ChatUserBo
    }

    override fun deleteUser(userId: Int): Boolean {
        val result = DBChatUser.delete {
            it.userId eq userId
        }
        return result == 1
    }

}