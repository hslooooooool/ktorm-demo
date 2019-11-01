package vip.qsos.ktorm.module.user.entity

import vip.qsos.ktorm.module.IVo

/**
 * @author : 华清松
 * 登录用户
 */
data class LoginUser(
        var userId: Int = -1,
        var userName: String,
        var account: String,
        var password: String,
        var avatar: String? = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",
        var birth: String? = null,
        var sexuality: Int? = null
) : IVo<TableLoginUser> {

    override fun toTable(): TableLoginUser {
        return TableLoginUser(
                userId = this.userId,
                account = this.account,
                password = this.password,
                userName = this.userName,
                avatar = this.avatar,
                birth = this.birth,
                sexuality = this.sexuality
        )
    }

    companion object {
        fun getVo(table: TableLoginUser?): LoginUser? {
            return table?.let {
                LoginUser(
                        userId = it.userId,
                        account = it.account,
                        password = it.password,
                        userName = it.userName,
                        avatar = it.avatar,
                        birth = it.birth,
                        sexuality = it.sexuality
                )
            }
        }
    }
}