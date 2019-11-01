package vip.qsos.ktorm.module.user.controller

import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.DBChatUser
import vip.qsos.ktorm.module.chat.entity.TableChatUser
import vip.qsos.ktorm.module.user.entity.DBLoginUser
import vip.qsos.ktorm.module.user.entity.LoginUser
import vip.qsos.ktorm.util.MResult

@RestController
@RequestMapping("/user")
class LoginUserController : ILoginUserModel {

    override fun login(account: String, password: String): MResult<LoginUser> {
        val user = LoginUser.getVo(DBLoginUser.findOne {
            (it.account eq account) and (it.password eq password)
        })
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
            val userId = DBLoginUser.add(LoginUser(
                    userName = account,
                    account = account,
                    password = password
            ).toTable()) as Int
            val ok = DBChatUser.add(TableChatUser(
                    userId = userId,
                    userName = account
            )) as Int
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