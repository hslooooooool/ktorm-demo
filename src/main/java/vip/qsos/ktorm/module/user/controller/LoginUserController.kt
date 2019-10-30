package vip.qsos.ktorm.module.user.controller

import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.DBChatUser
import vip.qsos.ktorm.module.user.entity.DBLoginUser
import vip.qsos.ktorm.module.user.entity.LoginUser
import vip.qsos.ktorm.util.MResult

@RestController
@RequestMapping("/user")
class LoginUserController : ILoginUserModel {

    override fun login(account: String, password: String): MResult<LoginUser> {
        val user = DBLoginUser.findOne {
            (it.account eq account) and (it.password eq password)
        }?.toLoginUser()
        return if (user == null) {
            MResult<LoginUser>().error("账号或密码错误")
        } else {
            MResult<LoginUser>().result(user)
        }
    }

    override fun register(account: String, password: String): MResult<LoginUser> {
        val user = DBLoginUser.findOne {
            (it.account eq account) and (it.password eq password)
        }
        return if (user == null) {
            val userId = DBLoginUser.insertAndGenerateKey {
                it.account to account
                it.password to password
                it.avatar to "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
            } as Int
            val ok = DBChatUser.insert {
                it.userId to userId
                it.userName to account
                it.avatar to "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
            }
            if (ok > 0) {
                MResult<LoginUser>().result(LoginUser(
                        userId = userId,
                        account = account,
                        password = password,
                        userName = account,
                        avatar = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
                ))
            } else {
                MResult<LoginUser>().error("注册失败")
            }
        } else {
            MResult<LoginUser>().error("用户已存在")
        }
    }

}