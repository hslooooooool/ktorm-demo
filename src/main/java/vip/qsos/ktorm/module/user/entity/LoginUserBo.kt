package vip.qsos.ktorm.module.user.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo

@ApiModel(description = "登录用户")
class LoginUserBo : IBo<TableLoginUser> {
    @ApiModelProperty(name = "userId", value = "用户ID")
    var userId: Int = -1
    @ApiModelProperty(name = "userName", value = "用户姓名")
    var userName: String = ""
    @ApiModelProperty(name = "account", value = "用户账号")
    var account: String = ""
    @ApiModelProperty(name = "password", value = "用户密码")
    var password: String = ""
    @ApiModelProperty(name = "avatar", value = "用户头像")
    var avatar: String? = null
    @ApiModelProperty(name = "birth", value = "出生日期")
    var birth: String? = null
    @ApiModelProperty(name = "sexuality", value = "性别,0女 1男 -1未知")
    var sexuality: Int = -1

    constructor()
    constructor(
            userId: Int = -1, userName: String, account: String, password: String,
            avatar: String? = null, birth: String? = null, sexuality: Int = -1
    ) {
        this.userId = userId
        this.userName = userName
        this.account = account
        this.password = password
        this.avatar = avatar
        this.birth = birth
        this.sexuality = sexuality
    }

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

    override fun getBo(table: TableLoginUser?): IBo<TableLoginUser>? {
        return table?.let {
            LoginUserBo(
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

    companion object {
        fun getVo(table: TableLoginUser?): LoginUserBo? {
            return table?.let {
                LoginUserBo(
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