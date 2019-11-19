package vip.qsos.ktorm.module.user.controller

import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.DBChatUser
import vip.qsos.ktorm.module.chat.entity.TableChatUser
import vip.qsos.ktorm.module.user.entity.DBLoginUser
import vip.qsos.ktorm.module.user.entity.LoginUserBo
import vip.qsos.ktorm.util.MResult

@RestController
class LoginUserController : ILoginUserModel {

    override fun login(account: String, password: String): MResult<LoginUserBo> {
        val user = LoginUserBo().getBo(
                DBLoginUser.findOne {
                    (it.account eq account) and (it.password eq password)
                }
        ) as LoginUserBo?
        return if (user == null) {
            MResult<LoginUserBo>().error("账号或密码错误")
        } else {
            MResult<LoginUserBo>().result(user)
        }
    }

    override fun register(account: String, password: String): MResult<LoginUserBo> {
        val t = DBLoginUser.findOne {
            (it.account eq account) and (it.password eq password)
        }
        if (t == null) {
            val user = LoginUserBo(
                    userName = account,
                    account = account,
                    password = password
            )
            val userId = DBLoginUser.add(user.toTable()) as Int
            user.userId = userId
            val ok = DBChatUser.add(
                    TableChatUser(
                            userId = userId,
                            userName = account
                    )
            ) as Int
            return if (ok > 0) {
                MResult<LoginUserBo>().result(user)
            } else {
                MResult<LoginUserBo>().error("注册失败")
            }
        } else {
            return MResult<LoginUserBo>().error("用户已存在")
        }
    }

}